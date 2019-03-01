package cn.xtits.bigdata

import cn.xtits.bigdata.entity.{LogMes, XtLog}
import cn.xtits.bigdata.util.{DataUtil, HBaseUtil}
import org.apache.hadoop.hbase.client.TableDescriptor
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext, kafka010}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

object StatStreamingApp {

  def main(args: Array[String]): Unit = {
    //HBaseUtil.createTable("test")
    //HBaseUtil.insertHTable("ClickCount","f1","getMaterial","count","1")
    val ssc = new StreamingContext("local[*]", "StatStreamingApp", Seconds(5))
    //    val sparkConf = new SparkConf().setAppName("StatStreamingApp")
    //    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "h1centos09:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "example",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topics = List("flumetopic").toSet
    val lines = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    ).map(s => {
      println(s.value())
      s.value().split('\t')
    }).filter(_.length > 7)
      .map(logArr => {
        val logMes = logArr(7).split('¤')
        if (logMes.length >= 5) {
          (logArr(0), logArr(1), logArr(2), logArr(3), logArr(4), logArr(5), logArr(7), logMes)
        } else {
          (logArr(0), logArr(1), logArr(2), logArr(3), logArr(4), logArr(5), logArr(7), ("log" + "¤" + logArr(7).toString().replace("¤", "@") + "¤0¤ ¤ ¤ ¤ ¤").split('¤'))
        }
      }).map(s => {
      val mes = LogMes(s._8(0), s._8(1), s._8(2).toLong, s._8(3))
      XtLog(s._1, s._2, s._3, s._4, s._5, s._6, s._7, mes)
    })

    //ResCount api访问数量和时间
    lines.filter(_.logMes.mesType == "url").map(s => {
      (s.logMes.url, (1L, s.logMes.duration, s.date))
    }).reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, if (a._3 > b._3) a._3 else b._3)).foreachRDD(rdd => {
      rdd.foreachPartition(partitions => {
        partitions.foreach(pair => {
          HBaseUtil.incrementColumnValue("ResCount", "f1", pair._1, "Count", pair._2._1)
          HBaseUtil.incrementColumnValue("ResCount", "f1", pair._1, "Duration", pair._2._2)

          HBaseUtil.incrementColumnValue("ResDailyCount", "f1", DataUtil.parseToDay(pair._2._3) + pair._1, "Count", pair._2._1)
          HBaseUtil.incrementColumnValue("ResDailyCount", "f1", DataUtil.parseToDay(pair._2._3) + pair._1, "Duration", pair._2._2)
        })
      })
    })

    //ResCount api访问数量
    lines.filter(_.logMes.mesType == "url").filter(_.logMes.duration > 100).map(s => {
      (s.logMes.url, s.logMes.duration, s.logMes.para, s.date)
    }).foreachRDD(rdd => {
      rdd.foreachPartition(partitions => {
        partitions.foreach(pair => {
          HBaseUtil.insertHTableLong("ResLongTime", "f1", DataUtil.parseToMs(pair._4) + pair._1, "Duration", pair._2)
          HBaseUtil.insertHTable("ResLongTime", "f1", DataUtil.parseToMs(pair._4) + pair._1, "Para", pair._3.toString)
        })
      })
    })

    //异常日志
    lines.filter(_.logMes.mesType == "error").map(s => {
      (s.logMes.url, s.logMes.duration, s.logMes.para, s.date, s.original)
    }).foreachRDD(rdd => {
      rdd.foreachPartition(partitions => {
        partitions.foreach(pair => {
          HBaseUtil.insertHTable("ResError", "f1", DataUtil.parseToMs(pair._4) + pair._1, "Para", pair._3.toString)
          HBaseUtil.insertHTable("ResError", "f1", DataUtil.parseToMs(pair._4) + pair._1, "Original", pair._5)
        })
      })
    })

    //用户系统日志
    lines.filter(_.logMes.mesType == "log").map(s => {
      (s.date, s.original, s.date)
    }).foreachRDD(rdd => {
      rdd.foreachPartition(partitions => {
        partitions.foreach(pair => {
          try {
            HBaseUtil.insertHTable("ResLog", "f1", DataUtil.parseToMs(pair._3), "Mes", pair._2)
          } catch {
            case ex: Exception => {
              println(ex.getMessage)
            }
          }
        })
      })
    })

    ssc.start();
    ssc.awaitTermination();
  }
}
