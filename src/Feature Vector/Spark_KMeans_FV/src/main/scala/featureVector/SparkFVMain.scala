package featureVector

import kMeansPipeline.CoreNLP
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{NGram, StopWordsRemover, Tokenizer, Word2Vec}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
  * Created by Mayanka on 21-Jun-16.
  */
object SparkFVMain {
  def main(args: Array[String]) {

    // Configuration
    val sparkConf = new SparkConf().setAppName("SparkFVMain").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    val spark = SQLContext.getOrCreate(sc)


    // Turn off Info Logger for Console
    Logger.getLogger("org").setLevel(Level.OFF);
    Logger.getLogger("akka").setLevel(Level.OFF);

    var hm = new HashMap[String, Int]()
    val IMAGE_CATEGORIES = List("sci.crypt", "sci.electronics", "sci.med", "sci.space")
    var index = 0
    IMAGE_CATEGORIES.foreach(f => {
      hm += IMAGE_CATEGORIES(index) -> index
      index += 1
    })
    val mapping = sc.broadcast(hm)
    // Read the file into RDD[String]
    val input = sc.wholeTextFiles("data/20_news_group/*", 500).map(line => {
      val location_array = line._1.split("/")
      val class_name = location_array(location_array.length - 2)
      var ff = line._2.replaceAll("[^a-zA-Z\\s:]", " ")
      ff = ff.replaceAll(":", "")
      //Getting Lemmatized Form of the word using CoreNLP
      val lemma = CoreNLP.returnLemma(ff)
      (mapping.value.get(class_name).get.toDouble, lemma)
    })


    //Creating DataFrame from RDD

    val sentenceData = spark.createDataFrame(input).toDF("labels", "sentence")

    //Tokenizer
    val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
    val wordsData = tokenizer.transform(sentenceData)

    //Stop Word Remover
    val remover = new StopWordsRemover()
      .setInputCol("words")
      .setOutputCol("filteredWords")
    val processedWordData = remover.transform(wordsData)

    //NGram
    val ngram = new NGram().setInputCol("filteredWords").setOutputCol("ngrams")
    val ngramDataFrame = ngram.transform(processedWordData)
    ngramDataFrame.take(3).foreach(println)
    println(ngramDataFrame.printSchema())

    val rddData = ngramDataFrame.select("labels", "filteredWords").rdd
      .map { case Row(labels: Double, filteredWords: mutable.WrappedArray[String]) => (labels, filteredWords.toArray) }

    val groupedData = rddData.groupByKey()

    val dataOutput = groupedData.collect().map(f => {
      val topWords = TFIDF.getTopTFIDFWords(sc, f._2, f._1)
      topWords
    })

    val outputRDD = sc.parallelize(dataOutput).flatMap(f => f.toList)
    //Word2Vec Model Generation

    val word2Vec = new Word2Vec()
      .setInputCol("filteredWords")
      .setOutputCol("result")
    val model = word2Vec.fit(ngramDataFrame)

    val output_df = model.getVectors.rdd
      .map { case Row(word: String, vector: Vector) => (word, vector) }

    val featureVector = output_df.join(outputRDD).map(f => {
        new LabeledPoint(f._2._2, f._2._1)
    })

    featureVector.take(1).foreach(println(_))
    val splits = featureVector.randomSplit(Array(0.6, 0.4), seed = 11L)
    val training = splits(0)
    val test = splits(1)
    val numClasses = IMAGE_CATEGORIES.length
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 5
    val maxBins = 32

    val DTmodel = DecisionTree.trainClassifier(training, numClasses, categoricalFeaturesInfo,
      impurity, maxDepth, maxBins)


    val predictionAndLabel = test.map(p => (DTmodel.predict(p.features), p.label))


    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()

    val metrics = new MulticlassMetrics(predictionAndLabel)
    // Confusion matrix
    println("Confusion matrix:")
    println(metrics.confusionMatrix)

    println("Accuracy: " + accuracy)





  }

}
