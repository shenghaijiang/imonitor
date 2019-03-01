package cn.xtits.bigdata.util

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat

object DataUtil {
    val YYYYMMDDHHMMSS_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS")
    val DAY_FORMAT = FastDateFormat.getInstance("yyyyMMdd")
    val MIN_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmm")
    val SECOND_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss")
    val MS_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmssSSS")

    /**
      * 把当前时间转换成时间戳
      *
      * @param time
      * @return
      */
    def getTime(time: String) = {
        YYYYMMDDHHMMSS_FORMAT.parse(time).getTime
    }

    def parseToDay(time: String) = {
        DAY_FORMAT.format(new Date(getTime(time)))
    }
    def parseToMin(time: String) = {
        MIN_FORMAT.format(new Date(getTime(time)))
    }
    def parseToSecond (time: String) = {
        SECOND_FORMAT.format(new Date(getTime(time)))
    }
    def parseToMs(time: String) = {
        MS_FORMAT.format(new Date(getTime(time)))
    }


    def main(args: Array[String]): Unit = {
        println(parseToDay("2017-11-20 00:39:26.123"))
        println(parseToMin("2017-11-20 00:39:26.123"))
        println(parseToSecond("2017-11-20 00:39:26.123"))
        println(parseToMs("2017-11-20 00:39:26.123"))
    }


}
