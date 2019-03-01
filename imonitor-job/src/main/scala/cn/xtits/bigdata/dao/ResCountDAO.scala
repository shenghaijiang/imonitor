/*
package com.study.spark.dao

import cn.xtits.bigdata.entity.ResCount
import cn.xtits.bigdata.util.HBaseUtil
import org.apache.hadoop.hbase.client.{Get, HTable}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer


object ResCountDAO {

     val tableName = "ResCount"
     val cf = "f1"
     val qualifer = "count"

    /**
      * 保存数据
      * @param list
      */
    def save(list:ListBuffer[ResCount]): Unit ={
      val table =  HBaseUtil.getInstance().getHtable(tableName)
        for(els <- list){
            table.incrementColumnValue(Bytes.toBytes(els.categaryID),Bytes.toBytes(cf),Bytes.toBytes(qualifer),els.clickCout);
        }

    }

    def count(day_categary:String) : Long={
        val table  =HBaseUtils.getInstance().getHtable(tableName)
        val get = new Get(Bytes.toBytes(day_categary))
        val  value =  table.get(get).getValue(Bytes.toBytes(cf), Bytes.toBytes(qualifer))
         if(value == null){
           0L
         }else{
             Bytes.toLong(value)
         }
    }

    def main(args: Array[String]): Unit = {
       val list = new ListBuffer[CategaryClickCount]
        list.append(CategaryClickCount("20171122_1",300))
        //list.append(CategaryClickCount("20171122_9", 60))
        //list.append(CategaryClickCount("20171122_10", 160))
        save(list)

      //  print(count("20171122_10")+"---")
    }

}
*/
