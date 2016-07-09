package featureVector

import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.mllib.regression.LabeledPoint

import scala.collection.immutable.HashMap

/**
  * Created by Mayanka on 22-Jun-16.
  */
object TFIDF {

  def getTopTFIDFWords(sc: SparkContext, input: Iterable[Array[String]], label: Double): Array[(String, Double)] = {

    val inpArr = new Array[Array[String]](input.size)
    input.copyToArray(inpArr)
    val inputData = sc.parallelize(inpArr).map(f => f.toSeq)
    val strData = sc.broadcast(inputData.toArray)
    val hashingTF = new HashingTF()
    val tf = hashingTF.transform(inputData)
    tf.cache()

    val idf = new IDF().fit(tf)
    val tfidf = idf.transform(tf)

    val tfidfvalues = tfidf.flatMap(f => {
      val ff: Array[String] = f.toString.replace(",[", ";").split(";")
      val values = ff(2).replace("]", "").replace(")", "").split(",")
      values
    })
    val tfidfindex = tfidf.flatMap(f => {
      val ff: Array[String] = f.toString.replace(",[", ";").split(";")
      val indices = ff(1).replace("]", "").replace(")", "").split(",")
      indices
    })
    tfidf.foreach(f => println(f))

    val tfidfData = tfidfindex.zip(tfidfvalues)
    var hm = new HashMap[String, Double]
    tfidfData.collect().foreach(f => {
      hm += f._1 -> f._2.toDouble
    })
    val mapp = sc.broadcast(hm)

    val documentData = inputData.flatMap(_.toList)
    val dd = documentData.map(f => {
      val i = hashingTF.indexOf(f)
      val h = mapp.value
      (f, h(i.toString))
    })

    val dd1 = dd.distinct().sortBy(_._2, false)
    dd1.take(20).foreach(f => {
      println(f)
    })
    val labelNo = sc.broadcast(label)
    return dd1.map(f => {
      (f._1, labelNo.value)
    }).take(10)
  }

  def getTFIDFVector(sc: SparkContext, input: Iterable[Array[String]], label: Double): Array[LabeledPoint] = {

    val inpArr = new Array[Array[String]](input.size)
    input.copyToArray(inpArr)
    val inputData = sc.parallelize(inpArr).map(f => f.toSeq)
    val strData = sc.broadcast(inputData.toArray)
    val hashingTF = new HashingTF()
    val tf = hashingTF.transform(inputData)
    tf.cache()

    val idf = new IDF().fit(tf)
    val tfidf = idf.transform(tf)

    val labelNo = sc.broadcast(label)

    val result = tfidf.map(f => {
      new LabeledPoint(labelNo.value, f)
    })

    result.collect()
  }

}
