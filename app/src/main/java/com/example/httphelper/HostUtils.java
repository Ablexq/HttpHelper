package com.example.httphelper;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.httphelplib.host.HostActivity;

public class HostUtils {

    // 点击次数
    private int count;
    // 规定有效时间
    private long duration;
    private Context context;
    private long[] mHits;

    private HostUtils(Builder builder) {
        count = builder.count;
        duration = builder.duration;
        context = builder.context;
        mHits = new long[count];
    }

    /**
     * 打开设置界面
     */
    public void continuousClick() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (SystemClock.uptimeMillis() - mHits[0] <= duration) {
            context.startActivity(new Intent(context, HostActivity.class));
        }
    }

    public static final class Builder {
        private int count = 5;
        private long duration = 1000;
        private Context context;

        public Builder() {
        }

        public Builder count(int val) {
            count = val;
            return this;
        }

        public Builder duration(long val) {
            duration = val;
            return this;
        }

        public Builder context(Context val) {
            context = val;
            return this;
        }

        public HostUtils build() {
            if (context == null) {
                throw new IllegalStateException("context can't be null");
            }
            return new HostUtils(this);
        }
    }
}
