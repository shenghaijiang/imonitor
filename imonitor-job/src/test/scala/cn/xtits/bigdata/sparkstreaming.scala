
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._ // not necessary since Spark 1.3


object sparkstreaming {
  def main(args: Array[String]) {
    println("s")
    println("Mapped [/auditevents || /auditevents.json],methods=[GET],pr".replace("|","@"))
  }
}