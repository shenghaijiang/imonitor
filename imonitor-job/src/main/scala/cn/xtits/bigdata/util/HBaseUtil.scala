package cn.xtits.bigdata.util

import java.io.IOException

import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes


object HBaseUtil {
  val conf = HBaseConfiguration.create();
  val connection = ConnectionFactory.createConnection(conf);
  val admin = connection.getAdmin();



  def createTable(tableName:String) = {
    val conf = HBaseConfiguration.create
    try {
      val conn = ConnectionFactory.createConnection(conf)
      val admin = conn.getAdmin
      try { //创建表名对象
        val table = TableName.valueOf(tableName)
        //创建表描述符对象
        val tbl = new HTableDescriptor(table)
        //创建列族描述符
        val col = new HColumnDescriptor("f1")
        tbl.addFamily(col)
        admin.createTable(tbl)

      } finally {
        if (conn != null) conn.close()
        if (admin != null) admin.close()
      }
    }
  }

  /**
    * 添加数据到hbase里面
    *
    * @param tableName 表名
    * @param rowKey    对应key的值
    * @param cf        hbase列簇
    * @param colum     hbase对应的列
    * @param value     hbase对应的值
    */
/*  def put(tableName: String, rowKey: String, cf: String, colum: String, value: String):Unit = {
    val table = connection.getTable(TableName.valueOf(tableName));
    val put = new Put(Bytes.toBytes(rowKey))
    put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(colum), Bytes.toBytes(value))
      table(put)
  }*/

  //插入记录
  def insertHTable(tablename:String,family:String,key:String,column:String,value:String):Unit={
    try{
      val userTable = TableName.valueOf(tablename)
      val table=connection.getTable(userTable)
      //准备key 的数据
      val p=new Put(key.getBytes)
      //为put操作指定 column 和 value
      p.addColumn(family.getBytes,column.getBytes,value.getBytes())
      //验证可以提交两个clomun？？？？不可以
      // p.addColumn(family.getBytes(),"china".getBytes(),"JAVA for china".getBytes())
      //提交一行
      table.put(p)
    }
  }

  //插入记录
  def insertHTableLong(tablename:String,family:String,key:String,column:String,value:Long):Unit={
    try{
      val userTable = TableName.valueOf(tablename)
      val table=connection.getTable(userTable)
      //准备key 的数据
      val p=new Put(key.getBytes)
      //为put操作指定 column 和 value
      p.addColumn(family.getBytes,column.getBytes,Bytes.toBytes(value))
      //验证可以提交两个clomun？？？？不可以
      // p.addColumn(family.getBytes(),"china".getBytes(),"JAVA for china".getBytes())
      //提交一行
      table.put(p)
    }
  }

  def incrementColumnValue(tablename:String,family:String,key:String,column:String,count:Long):Unit={

    try{
      val userTable = TableName.valueOf(tablename)
      val table=connection.getTable(userTable)
      table.incrementColumnValue(Bytes.toBytes(key),Bytes.toBytes(family),Bytes.toBytes(column),count);
    }
  }

  //插入记录
  def get(tablename:String,family:String,key:String,column:String):Long={
    try{
      val userTable = TableName.valueOf(tablename)
      val table=connection.getTable(userTable)

      //准备key 的数据
      val p = new Get(key.getBytes)
      var r=  Bytes.toLong(table.get(p).getValue(Bytes.toBytes(family),Bytes.toBytes(column)))
      r
    }
  }
  //插入记录
  def getResult(tablename:String,family:String,key:String):Result={
    try{
      val userTable = TableName.valueOf(tablename)
      val table=connection.getTable(userTable)

      //准备key 的数据
      val p = new Get(key.getBytes)
      table.get(p)
    }
  }
}
