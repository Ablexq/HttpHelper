package com.example.httphelplib.host;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;

import java.util.List;

public class LocalUtil {

    public static final String LocalHost = "LocalHost";

    public static void save(List<UrlEntity> list) {
        SPUtils.getInstance().put(LocalHost, JSON.toJSONString(list));
    }

    public static List<UrlEntity> get() {
        return JSON.parseArray(
                SPUtils.getInstance().getString(LocalHost),
                UrlEntity.class
        );
    }

}
