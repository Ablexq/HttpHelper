package com.example.httphelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.httphelplib.host.HostInterceptor;
import com.example.httphelplib.log.CaptureInfoInterceptor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HostInterceptor())
                .addInterceptor(new CaptureInfoInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .build();


        TextView textView = (TextView) this.findViewById(R.id.tv);
        textView.setText(BuildConfig.BASE_URL);

        final HostUtils hostUtils = new HostUtils.Builder()
                .context(this)
                .build();
        Button button = (Button) this.findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostUtils.continuousClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (BuildConfig.BASE_URL.contains("baidu")) {
            req("http://www.baidu.com/");
        } else if (BuildConfig.BASE_URL.contains("jd")) {
            req("https://www.jd.com/");
        }
    }

    private void req(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("2=========================" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("2=========================" + response.body().string());
            }
        });
    }
}
