package com.yepit.mapp.framework.util;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qianlong
 *
 * 日期处理工具类
 */
public class DateUtils {

    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date parseDate(String dateStr, String format) {
        if(StringUtils.isBlank(dateStr)){
            return new Date();
        }
        long time = parseTime(dateStr, format);
        Date date = new Date(time);
        return date;
    }

    private static long parseTime(String strTime, String format) {
        if (StringUtils.isBlank(format)) {
            format = "yyyy-mm-dd  HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long time = 0L;
        try {
            time = sdf.parse(strTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

//    public static void main(String[] args) throws Exception{
//        Date date = parseDate("2018-07-11","yyyy-MM-dd");
//        System.out.println(date.toString());
//    }
}
