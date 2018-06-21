package com.zrspring.libv2.network;

import com.zrspring.libv2.LibInit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shopping on 2017/12/22 10:02.
 * https://github.com/wheroj
 */

public class Client {

    private static final String BASE_URL = "http://183.230.40.68:7080/";

    public static <T> T getApi(final Class<T> service) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (LibInit.getInstance().getDebug()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else logging.setLevel(HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        okHttpClient = builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                //TODO 添加证书以后打开 支持https
//                .sslSocketFactory(LibInit.getInstance().getSslParams().sSLSocketFactory
//                        , LibInit.getInstance().getSslParams().trustManager)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request());
                    }
                })
                .addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(service);
    }
}

