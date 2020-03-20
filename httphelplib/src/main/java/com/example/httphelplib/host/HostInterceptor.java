package com.example.httphelplib.host;

import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HostInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl oldUrl = request.url();
        //https://wanandroid.com/wxarticle/chapters/json
        String oldScheme = oldUrl.scheme();//  https
        String oldHost = oldUrl.host();//   wanandroid.com
        int oldPort = oldUrl.port();//443
        String oldPath = oldUrl.encodedPath();//  /wxarticle/chapters/json
        String oldQuery = oldUrl.encodedQuery();//  null

        List<UrlEntity> mTotaList = new ArrayList<>();

        List<UrlEntity> localList = LocalUtil.get();
        if (localList != null && localList.size() > 0) {
            mTotaList = localList;
        }
        boolean isExist = false;
        if (mTotaList.size() > 0) {
            for (int i = 0; i < mTotaList.size(); i++) {
                UrlEntity urlEntity = mTotaList.get(i);
                if (!TextUtils.isEmpty(oldScheme) && !TextUtils.isEmpty(oldHost) &&
                        urlEntity.getScheme().equals(oldScheme) && urlEntity.getHost().equals(oldHost) &&
                        urlEntity.getPort() == oldPort) {
                    //scheme,host,port集合中添加过不再添加
                    isExist = true;
                    break;
                }
            }
        }
        if (!isExist) {
            //本地集合中未添加过，添加到集合
            mTotaList.add(new UrlEntity("默认", oldScheme, oldHost, oldPort, true));
            LocalUtil.save(mTotaList);
        }

        //重新配置url
        HttpUrl newUrl = null;
        if (mTotaList.size() > 0) {
            for (int i = 0; i < mTotaList.size(); i++) {
                UrlEntity urlEntity = mTotaList.get(i);
                boolean selected = urlEntity.isSelected();
                if (selected) {
                    newUrl = oldUrl
                            .newBuilder()
                            .scheme(urlEntity.getScheme())
                            .host(urlEntity.getHost())
                            .port(urlEntity.getPort())
                            .build();
                    break;
                }
            }
        }

        Request build = request.newBuilder()
                .url(newUrl == null ? oldUrl : newUrl)
                .build();

        return chain.proceed(build);
    }
}
