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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @ClassName: RegexUtils
* @Description: 注册验证工具类
* @author zrspring
* @date 2014-5-24 下午1:21:37
* @ConputerUserName zrspring
*/

public class RegexUtils {

	 /**
     * 验证Email
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?"; 
        return Pattern.matches(regex, email); 
    } 
     
    /**
     * 验证身份证号码
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isIdCard(String idCard) {
//        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}"; 
        
    	 String regex1 = "[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}"; 
    	 //String regex2 = "[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}"; 
    	 String regex3 = "[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Za-z])";
    	 if(Pattern.matches(regex1,idCard)){
    		 return true;
    	 }else if(Pattern.matches(regex3,idCard)){
    		 return true;
    	 }else{
    		return false; 
    	 }
    	 
    } 
     
    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * @param mobile 移动、联通、电信运营商的号码段
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *<p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isMobile(String mobile) {
        if(mobile==null){
            return false;
        }
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex,mobile); 
    } 
     
    /**
     * 验证固定电话号码
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *  数字之后是空格分隔的国家（地区）代码。</p>
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isTelephone(String phone) { 
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$"; 
        return Pattern.matches(regex, phone); 
    } 
     
    /**
     * 验证整数（正整数和负整数）
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex,digit);
    }

    /**
     * 验证是数字
     *
     */
    public static boolean isNumber(String number) {
        String regex = "^[0-9]*$";
        return Pattern.matches(regex,number);
    }


    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isDecimals(String decimals) { 
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?"; 
        return Pattern.matches(regex,decimals); 
    }  
     
    /**
     * 验证中文
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isChineseCharacter(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$"; 
        return Pattern.matches(regex,chinese); 
    } 
     

    /**
     * 验证URL地址
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean is3WUrl(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"; 
        return Pattern.matches(regex, url); 
    } 
    
    /**
     * 匹配中国邮政编码
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isPostcode(String postcode) { 
        String regex = "[1-9]\\d{5}"; 
        return Pattern.matches(regex, postcode); 
    } 
     
    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isIpAddress(String ipAddress) { 
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))"; 
        return Pattern.matches(regex, ipAddress); 
    } 
     
    /**
     * 匹配QQ号码
     * @param QQ QQ号码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isQQNumber(String QQ){
    	String regex = "^[\\d]{5,16}$"; 
    	return Pattern.matches(regex, QQ); 
    }
    
    /**
     * 密码可为纯数字和字母，也可数字和字母混合，不含特殊符号，位数必须大于6位小于15位 使用 regex="[a-zA-Z\\]\\[a-zA-Z0-9\\]{6,15}"
     * 密码可以全数字,可以全字母,可以全特殊字符(~!@#$%^&*.),三种的组合,可以是任意两种的组合,长度6-22.regex =/^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~]{6,22}$/
     * @param password 密码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPassword6To16(String password){
    	String regex ="^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,16}$";
    	return Pattern.matches(regex, password); 
    }
    
    /**
     * 可中文，可英文，可数字，可三者混合或者两者混合 "^[\\da-zA-Z0-9_]{5,16}$"
     * @param userName 需要验证的名字 
     * @return 验证成功返回true，验证失败返回false 
     */
    public static boolean isUserName5To16(String userName){//
    	String regex ="^[\\da-zA-Z0-9_]{5,16}$"; 
    	return Pattern.matches(regex, userName); 
    }
    

    /**
     * 检测银行卡号个数是否正确
     * @param bankCardNumber 16位或者19位
     * @return
     */
    public  static boolean isBankCardNumber(String bankCardNumber){
        String regex ="^(\\d{16}|\\d{19})$";
        return Pattern.matches(regex, bankCardNumber);
    }

    /**
     * 检查两次密码是否一致
     * @param pwd1
     * @param pwd2
     * @return
     */
    public static boolean isTwoPasswordMatch(String pwd1, String pwd2){
    	return pwd1.equals(pwd2);
    }

    /**
     * 验证是否含有中文字符
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
        Matcher m = p.matcher(str);
        return m.find();
    }

}
