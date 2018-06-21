package com.cmcc.pp.util;

/**
 * Created by ABC on 2018/1/26.
 */

public class JudgeErrorCodeUtils {
    private static String strTips = "";

    public static String getJudgeErrorTips(String message) {
        if (message.equals("-2")) {
            strTips = "服务器内部错误!";
        }else if (message.equals("-3")) {
            strTips = "验证码错误!";
        }else if (message.equals("-4")) {
            strTips = "账号或密码错误";
        }else if (message.equals("-5")) {
            strTips = "Token超时";
        }else if (message.equals("-6")) {
            strTips = "没有权限";
        }else if (message.equals("-101")) {
            strTips = "用户名为空";
        }else if (message.equals("-102")) {
            strTips = "密码为空";
        }else if (message.equals("-103")) {
            strTips = "验证码为空`";
        }else if (message.equals("-104")) {
            strTips = "验证码无效`";
        }else if (message.equals("-105")) {
            strTips = "验证码不匹配`";
        }else if (message.equals("-106")) {
            strTips = "OpenId为空`";
        }else if (message.equals("-107")) {
            strTips = "参数格式错误`";
        }else if (message.equals("-108")) {
            strTips = "参数为空`";
        }else if (message.equals("-150")) {
            strTips = "查询条件错误`";
        }else if (message.equals("-151")) {
            strTips = "调用服务失败`";
        }else if (message.equals("-152")) {
            strTips = "查询网关相关信息失败`";
        }else if (message.equals("-153")) {
            strTips = "获取网关信息失败`";
        }else if (message.equals("-154")) {
            strTips = "当前网关正在查询WAN信息,请稍后再试`";
        }else if (message.equals("-155")) {
            strTips = "网关查询WAN信息失败";
        }else if (message.equals("-156")) {
            strTips = "网关无匹配WAN信息";
        }else if (message.equals("-157")) {
            strTips = "当前网关正在诊断,请稍后再试";
        }
        return strTips;
    }

}
