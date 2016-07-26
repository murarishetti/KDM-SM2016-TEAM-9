/**
  * Created by chanti on 24-Jun-16.
  */

import org.apache.spark.{SparkConf, SparkContext}

object summarization {
  def main(args: Array[String]): Unit = {
//    val fileName = DataCollection.main();
  //  print(fileName)

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    summarizer.main()
    //CoreNLP.main(fileName);
  }
}
