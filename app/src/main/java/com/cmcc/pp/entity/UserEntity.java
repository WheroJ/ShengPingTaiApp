package com.cmcc.pp.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Version: 1.0
 * Author: li
 * CreatDate: 2017年12月11日10:04 33
 * Encoding:UTF-8
 * Description:
 */
public class UserEntity extends DataSupport{
    public String openId;//	用户编码

    @Column(unique = true, defaultValue = "unknown")
    public String telePhone;//手机号码

    public int expireIn;//token有效期	单位为秒
    public long tokenSaveTime;

    /**
     * 是否为当前登录账号： 1 否   2 是
     */
    public int currentLoginIn = 1;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserEntity) {
            return ((UserEntity) obj).telePhone.equals(this.telePhone);
        }
        return super.equals(obj);
    }
}
