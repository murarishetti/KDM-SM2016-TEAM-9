import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by chanti on 24-Jul-16.
  */
object kdm {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    summarize.main()
  }
}
