package com.example.httphelper;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求日志
 */
class LoggingInterceptor implements Interceptor {

    private static final String TAG = LoggingInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        //第一步，获得chain内的request
        Request request = chain.request();

        //处理request信息
        long t1 = System.nanoTime();
        Log.d(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        //第二步，用chain执行request,获取response
        Response response = chain.proceed(request);

        //处理response信息
        long t2 = System.nanoTime();
        Log.d(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        //第三步，返回response
        return response;
    }
}
