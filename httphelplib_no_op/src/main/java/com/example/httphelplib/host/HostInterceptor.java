package com.example.httphelplib.host;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class HostInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
