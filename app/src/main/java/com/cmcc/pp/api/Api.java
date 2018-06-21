package com.cmcc.pp.api;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shopping on 2018/1/24 15:20.
 * https://github.com/wheroj
 */

public interface Api {

    @POST("/amp/apply")
    Observable<ResponseBody> apply(@Body RequestBody requestBody);

    @POST("/amp/handle")
    Observable<ResponseBody> handle(@Body RequestBody requestBody);
}
