package com.zrspring.libv2.util;

import java.util.List;

/**
 * Version: 1.0
 * Author: rui
 * CreatDate: 2016年05月15日14:37 37
 * Encoding:UTF-8
 * Description:
 */
public class StrUtil {

    public static float getFolatByString(String intStr) {
        float result;
        try {
            result = Float.parseFloat(intStr);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static int getIntByString(String intStr) {
        int result;
        try {
            result = Integer.valueOf(intStr);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static double getDoubleByString(String intStr) {
        double result;
        try {
            result = Double.parseDouble(intStr);
        } catch (Exception e) {
            result = 0d;
        }
        return result;
    }

    //用 "," 拼接字符串
    public static String getStrLinkWithComma(List<String> listStrs) {
        StringBuffer result = new StringBuffer();
        if(listStrs!=null && listStrs.size()>0){
            int count = 0;
            for (String picServePath : listStrs) {
                result.append(picServePath);
                if (count != listStrs.size() - 1) {
                    result.append(",");
                }
                count++;
            }
        }
        return result.toString();
    }

    public static String rePlaclAllMark(String str) {
        return str.replaceAll("\n", "").replaceAll("\r", "");
    }

    public static long getLongByString(String intStr) {
        long result;
        try {
            result = Long.parseLong(intStr);
        } catch (Exception e) {
            result = 0l;
        }
        return result;
    }
}
