package com.wuxiaolong.androidmvpsample.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();

//        builder.addHeader("Cookie", SpUtils.GetConfigString("cookie"));

        return chain.proceed(builder.build());
    }
}
