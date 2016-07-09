/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kMeansPipeline

import java.io.PrintStream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel, RegexTokenizer, StopWordsRemover}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import scopt.OptionParser

import scala.collection.immutable.HashMap

object SparkKMeansMain {

  private case class Params(
                             input: Seq[String] = Seq.empty,
                             k: Int = 20,
                             numIterations: Int = 20
                           )

  def main(args: Array[String]) {
    val defaultParams = Params()

    val parser = new OptionParser[Params]("KMeansExample") {
      head("KMeansExample: an example KMeans app for plain text data.")
      opt[Int]("k")
        .text(s"number of topics. default: ${defaultParams.k}")
        .action((x, c) => c.copy(k = x))
      arg[String]("<input>...")
        .text("input paths (directories) to plain text corpora." +
          "  Each text file line should hold 1 document.")
        .unbounded()
        .required()
        .action((x, c) => c.copy(input = c.input :+ x))
    }

    parser.parse(args, defaultParams).map { params =>
      run(params)
    }.getOrElse {
      parser.showUsageAsError
      sys.exit(1)
    }
  }

  private def run(params: Params) {
    System.setProperty("hadoop.home.dir", "F:\\winutils")
    val conf = new SparkConf().setAppName(s"KMeansExample with $params").setMaster("local[*]").set("spark.driver.memory", "4g").set("spark.executor.memory", "4g")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    val topic_output = new PrintStream("data/Results.txt")
    // Load documents, and prepare them for KMeans.
    val preprocessStart = System.nanoTime()
    val (input, corpus, vocabArray) =
      preprocess(sc, params.input)
    corpus.cache()
    val actualCorpusSize = corpus.count()
    val actualVocabSize = vocabArray.length
    val preprocessElapsed = (System.nanoTime() - preprocessStart) / 1e9

    println()
    println(s"Corpus summary:")
    println(s"\t Training set size: $actualCorpusSize documents")
    println(s"\t Vocabulary size: $actualVocabSize terms")
    println(s"\t Preprocessing time: $preprocessElapsed sec")
    println()


    topic_output.println()
    topic_output.println(s"Corpus summary:")
    topic_output.println(s"\t Training set size: $actualCorpusSize documents")
    topic_output.println(s"\t Vocabulary size: $actualVocabSize terms")
    topic_output.println(s"\t Preprocessing time: $preprocessElapsed sec")
    topic_output.println()

    // Run KMeans.
    val startTime = System.nanoTime()

    val model = KMeans.train(corpus, params.k, params.numIterations)



    val elapsed = (System.nanoTime() - startTime) / 1e9

    println(s"Finished training KMeans model.  Summary:")
    println(s"\t Training time: $elapsed sec")


    topic_output.println(s"Finished training KMeans model.  Summary:")
    topic_output.println(s"\t Training time: $elapsed sec")

    val predictions = model.predict(corpus)

    val error = model.computeCost(corpus)
    val results = input.zip(corpus).zip(predictions)
    val resultsA = results.collect()
    var hm = new HashMap[Int, Int]
    resultsA.foreach(f => {
      topic_output.println(f._1._1 + ";" + f._1._2 + ";" + f._2)
      if (hm.contains(f._2)) {
        var v = hm.get(f._2).get
        v = v + 1
        hm += f._2 -> v
      }
      else {
        hm += f._2 -> 1
      }
    })

    topic_output.close()
    sc.stop()
  }

  private def preprocess(sc: SparkContext,
                         paths: Seq[String]): (RDD[(String, String)], RDD[Vector], Array[String]) = {

    val sqlContext = SQLContext.getOrCreate(sc)
    import sqlContext.implicits._
    val df = sc.wholeTextFiles(paths.mkString(",")).map(f => {
      var ff = f._2.replaceAll("[^a-zA-Z\\s:]", " ")
      ff = ff.replaceAll(":", "")
      // println(ff)
      (f._1, CoreNLP.returnLemma(ff))
    }).toDF("location", "docs")


    val tokenizer = new RegexTokenizer()
      .setInputCol("docs")
      .setOutputCol("rawTokens")
    val stopWordsRemover = new StopWordsRemover()
      .setInputCol("rawTokens")
      .setOutputCol("tokens")

    val countVectorizer = new CountVectorizer()
      .setInputCol("tokens")
      .setOutputCol("features")

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, stopWordsRemover, countVectorizer))

    val model = pipeline.fit(df)
    val documents = model.transform(df)
      .select("features")
      .rdd
      .map { case Row(features: Vector) => features }



    val input = model.transform(df).select("location", "docs").rdd.map { case Row(location: String, docs: String) => (location, docs) }
    println(model.transform(df).printSchema())
    val dd=model.transform(df).rdd.take(1)
      dd.foreach(println(_))
    (input, documents,
      model.stages(2).asInstanceOf[CountVectorizerModel].vocabulary)
  }
}

// scalastyle:on println
