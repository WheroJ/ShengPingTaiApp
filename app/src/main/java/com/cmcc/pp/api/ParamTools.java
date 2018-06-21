package com.cmcc.pp.api;

import com.cmcc.pp.entity.UserEntity;
import com.cmcc.pp.util.UIUtils;
import com.zrspring.libv2.util.VerifyUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.UUID;

/**
 * Created by shopping on 2017/12/22 14:35.
 * https://github.com/wheroj
 */

public class ParamTools {

    private final JSONObject params;
    private final JSONObject jsonObject;

    public ParamTools() {
        params = new JSONObject();
        jsonObject = new JSONObject();
        try {
            //1: Android  2:ios
            params.put("osType", "1");
            params.put("pVersion", UIUtils.getVersionCode());
            params.put("gid", UUID.randomUUID().toString().replace("-", ""));
            List<UserEntity> userEntityList = DataSupport.findAll(UserEntity.class);
            if (!userEntityList.isEmpty()) {
                params.put("openId", userEntityList.get(0).openId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addPath(String serviceName, String MethodName) {
        try {
            params.put("serviceName", serviceName);
            params.put("methodName", MethodName);
            String SequenceId = getRandomValue(8);
            params.put("sequenceId", SequenceId);
            VerifyUtils.INSTANCE.saveKey(SequenceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    /*产生numSize位16进制的数*/
    public static String getRandomValue(int numSize) {
        String str = "";
        for (int i = 0; i < numSize; i++) {
            char temp = 0;
            int key = (int) (Math.random() * 2);
            switch (key) {
                case 0:
                    temp = (char) (Math.random()* 10 + 48);//产生随机数字
                    break;
                case 1:
                    temp = (char) (Math.random()* 6 + 'a');//产生a-f
                    break;
                default:
                    break;
            }
            str = str + temp;
        }
        return str;
    }

    public void addParam(String name, Object value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void remove(String name) {
        jsonObject.remove(name);
    }

    public String getParam() {
        try {
            params.put("parameter", jsonObject);
            return params.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
