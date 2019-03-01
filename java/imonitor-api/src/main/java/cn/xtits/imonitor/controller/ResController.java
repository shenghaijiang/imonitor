package cn.xtits.imonitor.controller;

import cn.xtits.ireport.entity.ResCountCode;
import cn.xtits.ireport.entity.ResError;
import cn.xtits.ireport.entity.ResLog;
import cn.xtits.ireport.entity.ResLongTime;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtf.common.web.Pagination;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class ResController extends BaseController {

    // region ResCount

    @RequestMapping(value = "/resCount/listResCount")
    public AjaxResult listColorCode(
            @RequestParam(name = "url", required = false) String url,
            @RequestParam(name = "count", required = false) Integer count,
            @RequestParam(name = "duration", required = false) Integer duration,
            @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("1");
        String tableName = "ResCount";
        Configuration conf = HBaseConfiguration.create();
        try (Connection conn = ConnectionFactory.createConnection(conf)) {

            Table table = conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            if (url != null) {
                scan.setStartRow(Bytes.toBytes(url));
                scan.setStopRow(Bytes.toBytes(url + "9"));

            }

            if (count != null || duration != null) {
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                if (count != null) {
                    Filter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"), Bytes.toBytes("Count"), CompareFilter.CompareOp.GREATER_OR_EQUAL, new LongComparator(count));
                    filterList.addFilter(filter);
                }
                if (duration != null) {
                    Filter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"), Bytes.toBytes("Duration"), CompareFilter.CompareOp.GREATER_OR_EQUAL, new LongComparator(duration));
                    filterList.addFilter(filter);
                }
                scan.setFilter(filterList);
            }

            ResultScanner rs = null;
            List<ResCountCode> list = new ArrayList<>();
            int total = 0;
            try {
                rs = table.getScanner(scan);
                Iterator<Result> it = rs.iterator();

                while (it.hasNext()) {
                    Result r = it.next();
                    if (total >= (pageIndex - 1) * pageSize && total < pageIndex * pageSize) {
                        ResCountCode colorCode = new ResCountCode();
                        colorCode.setUrl(Bytes.toString(r.getRow()));
                        System.out.println(Bytes.toString(r.getRow()));
                        Map<byte[], byte[]> map = r.getFamilyMap(Bytes.toBytes("f1"));
                        for (Map.Entry<byte[], byte[]> entrySet : map.entrySet()) {
                            String col = Bytes.toString(entrySet.getKey());
                            String val = Bytes.toString(entrySet.getValue());
                            switch (col) {
                                case "Count":
                                    colorCode.setCount(Bytes.toLong(entrySet.getValue()));
                                    break;
                                case "Duration":
                                    colorCode.setDuration(Bytes.toLong(entrySet.getValue()));
                                    break;
                            }
                        }
                        list.add(colorCode);
                    }

                    total++;
                }
            } finally {
                rs.close();
            }
            Pagination<ResCountCode> pList = new Pagination<>(pageSize, pageIndex, list, total);
            return new AjaxResult(pList);

        }

    }
    // endregion

    // region ResDailyCount

    @RequestMapping(value = "/resDailyCount/listResDailyCount")
    public AjaxResult listResDailyCount(
            @RequestParam(name = "resDate", required = false) String resDate,
            @RequestParam(name = "url", required = false) String url,
            @RequestParam(name = "count", required = false) Integer count,
            @RequestParam(name = "duration", required = false) Integer duration,
            @RequestParam(name = "avgDuration", required = false) String avgDuration,
            @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws IOException {
        String tableName = "ResDailyCount";
        DateTime dt = DateTime.now();
        Configuration conf = HBaseConfiguration.create();
        try (Connection conn = ConnectionFactory.createConnection(conf)) {

            Table table = conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            if (count != null || duration != null) {
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                if (count != null) {
                    Filter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"), Bytes.toBytes("Count"), CompareFilter.CompareOp.GREATER_OR_EQUAL, new LongComparator(count));
                    filterList.addFilter(filter);
                }
                if (duration != null) {
                    Filter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"), Bytes.toBytes("Duration"), CompareFilter.CompareOp.GREATER_OR_EQUAL, new LongComparator(duration));
                    filterList.addFilter(filter);
                }
                scan.setFilter(filterList);
            }
            if (resDate != null) {
                dt = DateTime.parse(resDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            }

            if (url == null) {
                scan.setStartRow(Bytes.toBytes(dt.toString("yyyyMMdd")));
                scan.setStopRow(Bytes.toBytes(dt.plusDays(1).toString("yyyyMMdd")));

            } else {
                scan.setStartRow(Bytes.toBytes(dt.toString("yyyyMMdd") + url));
                scan.setStopRow(Bytes.toBytes(dt.toString("yyyyMMdd") + url + "0"));
            }
            ResultScanner rs = null;
            List<ResCountCode> list = new ArrayList<>();
            int total = 0;
            try {
                rs = table.getScanner(scan);
                Iterator<Result> it = rs.iterator();

                while (it.hasNext()) {
                    Result r = it.next();
                    if (total >= (pageIndex - 1) * pageSize && total < pageIndex * pageSize) {
                        ResCountCode colorCode = new ResCountCode();
                        colorCode.setUrl(Bytes.toString(r.getRow()).substring(8));
                        System.out.println(Bytes.toString(r.getRow()));
                        Map<byte[], byte[]> map = r.getFamilyMap(Bytes.toBytes("f1"));
                        for (Map.Entry<byte[], byte[]> entrySet : map.entrySet()) {
                            String col = Bytes.toString(entrySet.getKey());
                            String val = Bytes.toString(entrySet.getValue());
                            switch (col) {
                                case "Count":
                                    colorCode.setCount(Bytes.toLong(entrySet.getValue()));
                                    break;
                                case "Duration":
                                    colorCode.setDuration(Bytes.toLong(entrySet.getValue()));
                                    break;
                            }
                        }
                        colorCode.setAvgDuration(colorCode.getDuration().floatValue() / colorCode.getCount().floatValue());
                        list.add(colorCode);
                    }

                    total++;
                }
            } finally {
                rs.close();
            }
            Pagination<ResCountCode> pList = new Pagination<>(pageSize, pageIndex, list, total);
            return new AjaxResult(pList);

        }
    }
// endregion

    // region ResLongTime

    @RequestMapping(value = "/resLongTime/listResLongTime")
    public AjaxResult listResLongTime(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "duration", required = false) Integer duration,
            @RequestParam(name = "url", required = false) String url,
            @RequestParam(name = "para", required = false) String para,
            @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws IOException {
        String tableName = "ResLongTime";
        DateTime startDt = DateTime.now().plusDays(-2);
        DateTime endDt = DateTime.now().plusDays(1);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
        Configuration conf = HBaseConfiguration.create();
        try (Connection conn = ConnectionFactory.createConnection(conf)) {

            Table table = conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setReversed(true);

            if (duration != null) {
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                if (duration != null) {
                    Filter filter = new ValueFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL, new LongComparator(duration));
                    filterList.addFilter(filter);
                }
                scan.setFilter(filterList);
            }

            if (startDate != null) {
                startDt = DateTime.parse(startDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            }
            if (endDate != null) {
                endDt = DateTime.parse(endDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).plusDays(1);
            }
            scan.setStartRow(Bytes.toBytes(endDt.toString("yyyyMMdd000000000")));
            scan.setStopRow(Bytes.toBytes(startDt.toString("yyyyMMdd000000000")));
            //filterlist

            if (url != null || duration != null || para != null) {
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                if (url != null) {
                    RegexStringComparator regex = new RegexStringComparator(url);
                    Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, regex);
                    filterList.addFilter(filter);
                }

                if (duration != null) {
                    Filter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"), Bytes.toBytes("Duration"), CompareFilter.CompareOp.GREATER_OR_EQUAL, new LongComparator(duration));
                    filterList.addFilter(filter);
                }

                if (para != null) {
                    Filter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"), Bytes.toBytes("Para"), CompareFilter.CompareOp.EQUAL, new SubstringComparator(para));
                    filterList.addFilter(filter);
                }

                scan.setFilter(filterList);
            }

            ResultScanner rs = null;
            List<ResLongTime> list = new ArrayList<>();
            int total = 0;
            try {
                rs = table.getScanner(scan);
                Iterator<Result> it = rs.iterator();

                while (it.hasNext()) {
                    Result r = it.next();
                    if (total >= (pageIndex - 1) * pageSize && total < pageIndex * pageSize) {
                        ResLongTime colorCode = new ResLongTime();
                        colorCode.setUrl(Bytes.toString(r.getRow()));
                        System.out.println(Bytes.toString(r.getRow()));
                        Map<byte[], byte[]> map = r.getFamilyMap(Bytes.toBytes("f1"));
                        for (Map.Entry<byte[], byte[]> entrySet : map.entrySet()) {
                            String col = Bytes.toString(entrySet.getKey());
                            String val = Bytes.toString(entrySet.getValue());
                            switch (col) {
                                case "Para":
                                    colorCode.setPara(val);
                                    break;
                                case "Duration":
                                    colorCode.setDuration(Bytes.toLong(entrySet.getValue()));
                                    break;
                            }
                        }
                        colorCode.setResDate(DateTime.parse(colorCode.getUrl().substring(0, 17), format).toString("yyyy-MM-dd HH:mm:ss.SSS"));
                        colorCode.setUrl(colorCode.getUrl().substring(17));
                        list.add(colorCode);
                    }

                    total++;
                }
            } finally {
                rs.close();
            }
            Pagination<ResLongTime> pList = new Pagination<>(pageSize, pageIndex, list, total);
            return new AjaxResult(pList);

        }

    }
    // endregion

    // region ResError

    @RequestMapping(value = "/resError/listResError")
    public AjaxResult listResError(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "url", required = false) String url,
            @RequestParam(name = "original", required = false) String original,
            @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws IOException {
        String tableName = "ResError";
        DateTime startDt = DateTime.now().plusDays(-2);
        DateTime endDt = DateTime.now().plusDays(1);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
        Configuration conf = HBaseConfiguration.create();
        try (Connection conn = ConnectionFactory.createConnection(conf)) {

            Table table = conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setReversed(true);
            if (startDate != null) {
                startDt = DateTime.parse(startDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            }
            if (endDate != null) {
                endDt = DateTime.parse(endDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).plusDays(1);
            }
            scan.setStartRow(Bytes.toBytes(endDt.toString("yyyyMMdd000000000")));
            scan.setStopRow(Bytes.toBytes(startDt.toString("yyyyMMdd000000000")));

            //filterlist
            if (url != null || original != null) {
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                if (url != null) {
                    RegexStringComparator regex = new RegexStringComparator(url);
                    Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, regex);
                    filterList.addFilter(filter);

                }
                if (original != null) {
                    Filter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"), Bytes.toBytes("Original"), CompareFilter.CompareOp.GREATER_OR_EQUAL, new SubstringComparator(original));
                    filterList.addFilter(filter);
                }
                scan.setFilter(filterList);
            }


            ResultScanner rs = null;
            List<ResError> list = new ArrayList<>();
            int total = 0;
            try {
                rs = table.getScanner(scan);
                Iterator<Result> it = rs.iterator();

                while (it.hasNext()) {
                    Result r = it.next();
                    if (total >= (pageIndex - 1) * pageSize && total < pageIndex * pageSize) {
                        ResError colorCode = new ResError();
                        colorCode.setUrl(Bytes.toString(r.getRow()));
                        System.out.println(Bytes.toString(r.getRow()));
                        Map<byte[], byte[]> map = r.getFamilyMap(Bytes.toBytes("f1"));
                        for (Map.Entry<byte[], byte[]> entrySet : map.entrySet()) {
                            String col = Bytes.toString(entrySet.getKey());
                            String val = Bytes.toString(entrySet.getValue());
                            switch (col) {
                                case "Para":
                                    colorCode.setPara(val);
                                    break;
                                case "Original":
                                    colorCode.setOriginal(val);
                                    break;
                            }
                        }
                        colorCode.setResDate(DateTime.parse(colorCode.getUrl().substring(0, 17), format).toString("yyyy-MM-dd HH:mm:ss.SSS"));
                        colorCode.setUrl(colorCode.getUrl().substring(17));
                        list.add(colorCode);
                    }

                    total++;
                }
            } finally {
                rs.close();
            }
            Pagination<ResError> pList = new Pagination<>(pageSize, pageIndex, list, total);
            return new AjaxResult(pList);

        }
    }
    // endregion

    // region ResLog

    @RequestMapping(value = "/resLog/listResLog")
    public AjaxResult listLog(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "mes", required = false) String mes,
            @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws IOException {
        String tableName = "ResLog";
        DateTime startDt = DateTime.now().plusDays(-6);
        DateTime endDt = DateTime.now().plusDays(1);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
        Configuration conf = HBaseConfiguration.create();
        try (Connection conn = ConnectionFactory.createConnection(conf)) {

            Table table = conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setReversed(true);
            if (startDate != null) {
                startDt = DateTime.parse(startDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            }
            if (endDate != null) {
                endDt = DateTime.parse(endDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).plusDays(1);
            }
            scan.setStartRow(Bytes.toBytes(endDt.toString("yyyyMMdd000000000")));
            scan.setStopRow(Bytes.toBytes(startDt.toString("yyyyMMdd000000000")));

            if (mes != null) {
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                if (mes != null) {
                    Filter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"), Bytes.toBytes("Mes"), CompareFilter.CompareOp.EQUAL, new SubstringComparator(mes));
                    filterList.addFilter(filter);
                }
                scan.setFilter(filterList);
            }

            ResultScanner rs = null;
            List<ResLog> list = new ArrayList<>();
            int total = 0;
            try {
                rs = table.getScanner(scan);
                Iterator<Result> it = rs.iterator();

                while (it.hasNext()) {
                    Result r = it.next();
                    if (total >= (pageIndex - 1) * pageSize && total < pageIndex * pageSize) {
                        ResLog colorCode = new ResLog();
                        colorCode.setResDate(DateTime.parse(Bytes.toString(r.getRow()), format).toString("yyyy-MM-dd HH:mm:ss.SSS"));
                        System.out.println(Bytes.toString(r.getRow()));
                        Map<byte[], byte[]> map = r.getFamilyMap(Bytes.toBytes("f1"));
                        for (Map.Entry<byte[], byte[]> entrySet : map.entrySet()) {
                            String col = Bytes.toString(entrySet.getKey());
                            String val = Bytes.toString(entrySet.getValue());
                            switch (col) {
                                case "Mes":
                                    colorCode.setMes(val);
                                    break;
                            }
                        }
                        list.add(colorCode);
                    }

                    total++;
                }
            } finally {
                rs.close();
            }
            Pagination<ResLog> pList = new Pagination<>(pageSize, pageIndex, list, total);
            return new AjaxResult(pList);

        }
    }
// endregion
}