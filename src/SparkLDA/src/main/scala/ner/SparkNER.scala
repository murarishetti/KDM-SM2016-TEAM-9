package ner

import CoreNLP
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Mayanka on 29-Jun-16.
  */
object SparkNER {
  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "F:\\winutils")
    val conf = new SparkConf().setAppName(s"NERTrain").setMaster("local[*]").set("spark.driver.memory", "4g").set("spark.executor.memory", "4g")
    val sc = new SparkContext(conf)


    val Domain_Based_Words = sc.broadcast(sc.textFile("data/ner/domainBasedWords").map(CoreNLP.returnLemma(_)).collect())

    val input = sc.textFile("data/ner/sample").flatMap(f => {
      val s=f.replace("[^a-zA-Z\\s]","")
      s.split(" ")}).map(ff => {
      val f = CoreNLP.returnLemma(ff)
      if (Domain_Based_Words.value.contains(f))
        (f, "CS")
      else
        (f, CoreNLP.returnNER(f))
    }

    )

    println(input.collect().mkString("\n"))
  }

}
