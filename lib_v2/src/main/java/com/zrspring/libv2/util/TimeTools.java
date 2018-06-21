package com.zrspring.libv2.util;
/*   
 * Copyright (c) 2014-2014 China.ChongQing.MaiQuan Ltd All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */

import android.content.Context;
import android.content.res.Resources;

import com.zrspring.libv2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zrspring
 * @ClassName: TimeTools
 * @Description: 时间工具类
 * @date 2014-5-24 下午1:12:32
 * @ConputerUserName zrspring
 */

public class TimeTools {

    /**
     * 返回时间字符串
     *
     * @param timeInMillis 时间
     * @param dateFormat   格式
     * @return String 时间字符串
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 返回指定毫秒数的时间，时间格式为："yyyy-MM-dd HH:mm:ss"
     *
     * @param timeInMillis 指定的时间毫秒数
     * @return String 时间字符串
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, getDATA_YYYY_MM_DD_HH_MM_SS());
    }

    /**
     * @return long 格林威治标准时间
     * @throws
     * @Title: getCurrentTimeInLong
     * @Description: 返回当前系统的时间毫秒数
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * @param dateFormat 时间格式
     * @return String 时间字符串
     * @throws
     * @Title: getCurrentTimeInString
     * @Description: 传入时间格式，并获取当前格式下，当前的时间字符串
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * @MethodthName: getTimeByStringTimeMillis
     * @params: [context, strStringTimeMillis]
     * @Return:java.lang.String
     * @Throw:
     * @Description: 根据时间戳，返回，XX时间前，刚刚等，字符
     */
    public static String getTimeByStringTimeMillis(Context context, String strStringTimeMillis) {
        StringBuffer sb = new StringBuffer();
        String strTime = strStringTimeMillis;
        if (strStringTimeMillis.length() <= 10) {
            strTime = strTime + "000";
        }
        Resources rs = context.getResources();
        long strTimeMillis = Long.parseLong(strTime);
        long crurentMillis = getCurrentTimeInLong();
        long spaciMillis = crurentMillis - strTimeMillis;

        long mill = (long) Math.ceil(spaciMillis / 1000);//秒前
        long minute = (long) Math.ceil(spaciMillis / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(spaciMillis / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(spaciMillis / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            if (day > 1) {
                sb.append(getTime(strTimeMillis));
            } else {
                sb.append(day + rs.getString(R.string.time_tools_space_day_up));
            }
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append(rs.getString(R.string.time_tools_space_day));//"1天"
            } else {
                sb.append(hour + rs.getString(R.string.time_tools_space_houres_up));//小时
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append(rs.getString(R.string.time_tools_space_houres));//"1小时"
            } else {
                sb.append(minute + rs.getString(R.string.time_tools_space_minute_up));//"分钟"
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append(rs.getString(R.string.time_tools_space_minute));//"1分钟"
            } else {
                // sb.append(mill + "秒");
                sb.append(rs.getString(R.string.time_tools_space_just));
            }
        } else {
            sb.append(rs.getString(R.string.time_tools_space_just));
        }
        return sb.toString();
    }

    //=================================
    //
    //=================================

    public static SimpleDateFormat getDATA_YYYY_MM_DD_HH_MM_SS() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static SimpleDateFormat getDATA_YYYY_MM_DD() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static SimpleDateFormat getDATA_HH_MM_SS() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    public static SimpleDateFormat getMM_SS() {
        return new SimpleDateFormat("mm:ss");
    }

    public static SimpleDateFormat getHH_MM() {
        return new SimpleDateFormat("HH:mm");
    }

    public static SimpleDateFormat get_MM_DD_HH_MM() {
        return new SimpleDateFormat("MM-dd HH:mm");
    }

    public static SimpleDateFormat get_MM_DD_HH_MM2() {
        return new SimpleDateFormat("MM/dd HH:mm");
    }

    public static String getTimeFromPhpTimeStr(String phpTimeStr, SimpleDateFormat dateFormat) {
        String time;
        long data;
        if (phpTimeStr.length() < 13) {
            time = phpTimeStr + "000";
        } else {
            time = phpTimeStr;
        }
        try {
            data = Long.parseLong(time);
        } catch (Exception e) {
            data = 0l;
        }
        return dateFormat.format(new Date(data));
    }

    public static String getCurrentPhpTimeStr() {
        String result = null;
        String timelong = String.valueOf(getCurrentTimeInLong());
        if (timelong.length() > 10) {
            result = timelong.substring(0, 9);
        }
        return result;
    }

    private static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static int getCurrentYearInt() {
        return getCalendar().get(Calendar.YEAR);
    }

    public static int getCurrentMonthInt() {
        return (getCalendar().get(Calendar.MONTH) + 1);
    }

    public static String getCurrentYearString() {
        return String.valueOf(getCalendar().get(Calendar.YEAR));
    }

    public static String getCurrentMonthString() {
        return String.valueOf((getCalendar().get(Calendar.MONTH) + 1));
    }


    public static String getMM_DD_HH_MMByTimeStr(String timeStrs) {
        //2017-02-05 15:00
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formatMMDDHHMM = get_MM_DD_HH_MM();
        Date date = null;
        long datelong = 0l;
        try {
            date = sdf.parse(timeStrs);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            datelong = date.getTime();
        }
        return getTime(datelong, formatMMDDHHMM);
    }
//    Calendar cal = Calendar.getInstance();
//    int day = cal.get(Calendar.DATE);
//    int month = cal.get(Calendar.MONTH) + 1;
//    int year = cal.get(Calendar.YEAR);
//    int dow = cal.get(Calendar.DAY_OF_WEEK);
//    int dom = cal.get(Calendar.DAY_OF_MONTH);
//    int doy = cal.get(Calendar.DAY_OF_YEAR);
//
//    System.out.println("当期时间: " + cal.getTime());
//    System.out.println("日期: " + day);
//    System.out.println("月份: " + month);
//    System.out.println("年份: " + year);
//    System.out.println("一周的第几天: " + dow);  // 星期日为一周的第一天输出为 1，星期一输出为 2，以此类推
//    System.out.println("一月中的第几天: " + dom);
//    System.out.println("一年的第几天: " + doy);

}