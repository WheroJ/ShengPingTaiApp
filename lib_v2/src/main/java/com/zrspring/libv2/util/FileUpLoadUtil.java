package com.zrspring.libv2.util;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Version: 1.0
 * Author: rui
 * CreatDate: 2016年05月26日上午9:09 09
 * Encoding:UTF-8
 * Description: 文件上传工具类
 */
public class FileUpLoadUtil {



    public static void UpLoadFile(File file, UploadListenner upLoadListenner) {
        OkHttpUtils.post()
                .addFile("filedata", file.getName(), file)
                .url("http://msinter.cheson.net/api/upload.php")
//                .params(params)//
//                .headers(headers)//
                .build()//
                .execute(upLoadListenner);
    }

    //
    public abstract static class UploadListenner extends Callback<String> {

        @Override
        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            onUpLoadInProgress(progress);
        }

        @Override
        public String parseNetworkResponse(Response response, int i) throws Exception {
            String str = response.body().string();
            if(response.code() == 200){
                onUpLoadSuccess(str);
            }else{
                onUpLoadFailed(str);
            }
            return str;
        }

        @Override
        public void onError(Call call, Exception e, int i) {
            if(e!=null){
                e.printStackTrace();
                onUpLoadFailed(e.getMessage());
            }
        }

        public abstract void onUpLoadFailed(String str);
        public abstract void onUpLoadSuccess(String str);
        public abstract void onUpLoadInProgress(float count);

    }

    /**
     * @MethodthName: UpLoadSingleFile
     * @params: [upLoadName, file, url, upLoadListenner]
     * @Return:void
     * @Throw:
     * @Description: 单个文件上传
     */
    public static void UpLoadSingleFile(String paramValues,
                                        File file,
                                        String url,
                                        UploadListenner upLoadListenner) {
        PostFormBuilder build = OkHttpUtils.post();
        build.addFile(paramValues, file.getName(), file);
        build.url(url);
//               build.params(params)//
//               build.headers(headers)//
        build.build().execute(upLoadListenner);
    }

    public static String getPathFromResponseString(String responseStr) {
        String resultPath;
        try {
            resultPath = new JSONObject(responseStr).optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            resultPath = "";
        }
        return resultPath;
    }

}
