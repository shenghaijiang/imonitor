//package cn.xtits.ireport.config;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.apache.hadoop.hbase.client.HTable;
//import org.apache.hadoop.hbase.client.Result;
//import org.apache.hadoop.hbase.client.ResultScanner;
//import org.apache.hadoop.hbase.client.Scan;
//import org.apache.hadoop.hbase.filter.FilterList;
//import org.apache.hadoop.hbase.filter.PageFilter;
//import org.apache.hadoop.hbase.util.Bytes;
//
///**
// * Description: HBase表数据分页模型类。
// */
//public class HBasePageModel implements Serializable {
//    private static final long serialVersionUID = 330410716100946538L;
//    private int pageSize = 100;
//    private int pageIndex = 0;
//    private int prevPageIndex = 1;
//    private int nextPageIndex = 1;
//    private int pageCount = 0;
//    private int pageFirstRowIndex = 1;
//    private byte[] pageStartRowKey = null;
//    private byte[] pageEndRowKey = null;
//    private boolean hasNextPage = true;
//    private int queryTotalCount = 0;
//    private long startTime = System.currentTimeMillis();
//    private long endTime = System.currentTimeMillis();
//    private List<Result> resultList = new ArrayList<Result>();
//    public HBasePageModel(int pageSize) {
//        this.pageSize = pageSize;
//    }
//    /**
//     * 获取分页记录数量
//     * @return
//     */
//    public int getPageSize() {
//        return pageSize;
//    }
//    /**
//     * 设置分页记录数量
//     * @param pageSize
//     */
//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }
//    /**
//     * 获取当前页序号
//     * @return
//     */
//    public int getPageIndex() {
//        return pageIndex;
//    }
//    /**
//     * 设置当前页序号
//     * @param pageIndex
//     */
//    public void setPageIndex(int pageIndex) {
//        this.pageIndex = pageIndex;
//    }
//    /**
//     * 获取分页总数
//     * @return
//     */
//    public int getPageCount() {
//        return pageCount;
//    }
//    /**
//     * 设置分页总数
//     * @param pageCount
//     */
//    public void setPageCount(int pageCount) {
//        this.pageCount = pageCount;
//    }
//    /**
//     * 获取每页的第一行序号
//     * @return
//     */
//    public int getPageFirstRowIndex() {
//        this.pageFirstRowIndex = (this.getPageIndex() - 1) * this.getPageSize() + 1;
//        return pageFirstRowIndex;
//    }
//    /**
//     * 获取每页起始行键
//     * @return
//     */
//    public byte[] getPageStartRowKey() {
//        return pageStartRowKey;
//    }
//    /**
//     * 设置每页起始行键
//     * @param pageStartRowKey
//     */
//    public void setPageStartRowKey(byte[] pageStartRowKey) {
//        this.pageStartRowKey = pageStartRowKey;
//    }
//    /**
//     * 获取每页结束行键
//     * @return
//     */
//    public byte[] getPageEndRowKey() {
//        return pageEndRowKey;
//    }
//    /**
//     * 设置每页结束行键
//     * @param pageEndRowKey
//     */
//    public void setPageEndRowKey(byte[] pageEndRowKey) {
//        this.pageEndRowKey = pageEndRowKey;
//    }
//    /**
//     * 获取上一页序号
//     * @return
//     */
//    public int getPrevPageIndex() {
//        if(this.getPageIndex() > 1) {
//            this.prevPageIndex = this.getPageIndex() - 1;
//        } else {
//            this.prevPageIndex = 1;
//        }
//        return prevPageIndex;
//    }
//    /**
//     * 获取下一页序号
//     * @return
//     */
//    public int getNextPageIndex() {
//        this.nextPageIndex = this.getPageIndex() + 1;
//        return nextPageIndex;
//    }
//    /**
//     * 获取是否有下一页
//     * @return
//     */
//    public boolean isHasNextPage() {
////这个判断是不严谨的，因为很有可能剩余的数据刚好够一页。
//        if(this.getResultList().size() == this.getPageSize()) {
//            this.hasNextPage = true;
//        } else {
//            this.hasNextPage = false;
//        }
//        return hasNextPage;
//    }
//    /**
//     * 获取已检索总记录数
//     */
//    public int getQueryTotalCount() {
//        return queryTotalCount;
//    }
//    /**
//     * 获取已检索总记录数
//     * @param queryTotalCount
//     */
//    public void setQueryTotalCount(int queryTotalCount) {
//        this.queryTotalCount = queryTotalCount;
//    }
//    /**
//     * 初始化起始时间（毫秒）
//     */
//    public void initStartTime() {
//        this.startTime = System.currentTimeMillis();
//    }
//    /**
//     * 初始化截止时间（毫秒）
//     */
//    public void initEndTime() {
//        this.endTime = System.currentTimeMillis();
//    }
//    /**
//     * 获取毫秒格式的耗时信息
//     * @return
//     */
//    public String getTimeIntervalByMilli() {
//        return String.valueOf(this.endTime - this.startTime) + "毫秒";
//    }
//    /**
//     * 获取秒格式的耗时信息
//     * @return
//     */
//    public String getTimeIntervalBySecond() {
//        double interval = (this.endTime - this.startTime)/1000.0;
//        DecimalFormat df = new DecimalFormat("#.##");
//        return df.format(interval) + "秒";
//    }
//    /**
//     * 打印时间信息
//     */
//    public void printTimeInfo() {
//        System.out.println("起始时间：" + this.startTime);
//        System.out.println("截止时间：" + this.endTime);
//        System.out.println("耗费时间：" + this.getTimeIntervalBySecond());
//    }
//    /**
//     * 获取HBase检索结果集合
//     * @return
//     */
//    public List<Result> getResultList() {
//        return resultList;
//    }
//    /**
//     * 设置HBase检索结果集合
//     * @param resultList
//     */
//    public void setResultList(List<Result> resultList) {
//        this.resultList = resultList;
//    }
//
//
//
//
//    /**
//     * 分页检索表数据。<br>
//     * （如果在创建表时为此表指定了非默认的命名空间，则需拼写上命名空间名称，格式为【namespace:tablename】）。
//     * @param tableName 表名称(*)。
//     * @param startRowKey 起始行键(可以为空，如果为空，则从表中第一行开始检索)。
//     * @param endRowKey 结束行键(可以为空)。
//     * @param filterList 检索条件过滤器集合(不包含分页过滤器；可以为空)。
//     * @param maxVersions 指定最大版本数【如果为最大整数值，则检索所有版本；如果为最小整数值，则检索最新版本；否则只检索指定的版本数】。
//     * @param pageModel 分页模型(*)。
//     * @return 返回HBasePageModel分页对象。
//     */
//    public static HBasePageModel scanResultByPageFilter(String tableName, byte[] startRowKey, byte[] endRowKey, FilterList filterList, int maxVersions, HBasePageModel pageModel) throws IOException {
//
//        Configuration conf = HBaseConfiguration.create();
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//        if(pageModel == null) {
//            pageModel = new HBasePageModel(10);
//        }
//        if(maxVersions <= 0 ) {
//            //默认只检索数据的最新版本
//            maxVersions = Integer.MIN_VALUE;
//        }
//        pageModel.initStartTime();
//        pageModel.initEndTime();
//
//        try {
//            int tempPageSize = pageModel.getPageSize();
//            boolean isEmptyStartRowKey = false;
//            if(startRowKey == null) {
//                //则读取表的第一行记录，这里用到了笔者本人自己构建的一个表数据操作类。
//                Result firstResult = selectFirstResultRow(tableName, filterList);
//                if(firstResult.isEmpty()) {
//                    return pageModel;
//                }
//                startRowKey = firstResult.getRow();
//            }
//            if(pageModel.getPageStartRowKey() == null) {
//                isEmptyStartRowKey = true;
//                pageModel.setPageStartRowKey(startRowKey);
//            } else {
//                if(pageModel.getPageEndRowKey() != null) {
//                    pageModel.setPageStartRowKey(pageModel.getPageEndRowKey());
//                }
//                //从第二页开始，每次都多取一条记录，因为第一条记录是要删除的。
//                tempPageSize += 1;
//            }
//
//            Scan scan = new Scan();
//            scan.setStartRow(pageModel.getPageStartRowKey());
//            if(endRowKey != null) {
//                scan.setStopRow(endRowKey);
//            }
//            PageFilter pageFilter = new PageFilter(pageModel.getPageSize() + 1);
//            if(filterList != null) {
//                filterList.addFilter(pageFilter);
//                scan.setFilter(filterList);
//            } else {
//                scan.setFilter(pageFilter);
//            }
//            if(maxVersions == Integer.MAX_VALUE) {
//                scan.setMaxVersions();
//            } else if(maxVersions == Integer.MIN_VALUE) {
//
//            } else {
//                scan.setMaxVersions(maxVersions);
//            }
//            ResultScanner scanner = table.getScanner(scan);
//            List<Result> resultList = new ArrayList<Result>();
//            int index = 0;
//            for(Result rs : scanner.next(tempPageSize)) {
//                if(isEmptyStartRowKey == false && index == 0) {
//                    index += 1;
//                    continue;
//                }
//                if(!rs.isEmpty()) {
//                    resultList.add(rs);
//                }
//                index += 1;
//            }
//            scanner.close();
//            pageModel.setResultList(resultList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                table.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        int pageIndex = pageModel.getPageIndex() + 1;
//        pageModel.setPageIndex(pageIndex);
//        if(pageModel.getResultList().size() > 0) {
//            //获取本次分页数据首行和末行的行键信息
//            byte[] pageStartRowKey = pageModel.getResultList().get(0).getRow();
//            byte[] pageEndRowKey = pageModel.getResultList().get(pageModel.getResultList().size() - 1).getRow();
//            pageModel.setPageStartRowKey(pageStartRowKey);
//            pageModel.setPageEndRowKey(pageEndRowKey);
//        }
//        int queryTotalCount = pageModel.getQueryTotalCount() + pageModel.getResultList().size();
//        pageModel.setQueryTotalCount(queryTotalCount);
//        pageModel.initEndTime();
//        pageModel.printTimeInfo();
//        return pageModel;
//    }
//
//    /**
//     * 检索指定表的第一行记录。<br>
//     * （如果在创建表时为此表指定了非默认的命名空间，则需拼写上命名空间名称，格式为【namespace:tablename】）。
//     * @param tableName 表名称(*)。
//     * @param filterList 过滤器集合，可以为null。
//     * @return
//     */
//    public static Result selectFirstResultRow(String tableName,FilterList filterList) throws IOException {
//
//        Configuration conf = HBaseConfiguration.create();
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//
//        try {
//            Scan scan = new Scan();
//            if(filterList != null) {
//                scan.setFilter(filterList);
//            }
//            ResultScanner scanner = table.getScanner(scan);
//            Iterator<Result> iterator = scanner.iterator();
//            int index = 0;
//            while(iterator.hasNext()) {
//                Result rs = iterator.next();
//                if(index == 0) {
//                    scanner.close();
//                    return rs;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                table.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//}