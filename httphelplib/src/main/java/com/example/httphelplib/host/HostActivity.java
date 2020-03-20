package com.example.httphelplib.host;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.example.httphelplib.R;

import java.util.ArrayList;
import java.util.List;

public class HostActivity extends AppCompatActivity {

    private HostAdapter myAdapter;
    private List<UrlEntity> mList = new ArrayList<>();
    private EditText etHost;
    private EditText etPort;
    private Button btn;
    private EditText etScheme;
    private EditText etName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        RecyclerView recyclerView = this.findViewById(R.id.rv);
        etName = findViewById(R.id.etName);
        etScheme = findViewById(R.id.etScheme);
        etHost = findViewById(R.id.etHost);
        etPort = findViewById(R.id.etPort);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String scheme = etScheme.getText().toString().trim();
                String host = etHost.getText().toString().trim();

                int port = -1;
                try {
                    port = Integer.parseInt(etPort.getText().toString().trim());
                } catch (Exception e) {
                    Toast.makeText(HostActivity.this, "端口格式不正确", Toast.LENGTH_SHORT).show();
                }
                if (port == -1) {
                    if (scheme.contains("https")) {
                        port = 443;
                    } else if (scheme.contains("http")) {
                        port = 80;
                    }
                }
                //新增
                mList.add(new UrlEntity(name, scheme, host, port, false));
                LocalUtil.save(mList);
                myAdapter.setData(mList);
            }
        });

        myAdapter = new HostAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnClickItemListener(new HostAdapter.OnClickItemListener() {
            @Override
            public void onChooseClick(int position) {
                //选择域名
                for (int i = 0; i < mList.size(); i++) {
                    UrlEntity urlEntity = mList.get(i);
                    urlEntity.setSelected(false);
                }
                UrlEntity clickEntity = mList.get(position);
                clickEntity.setSelected(true);
                LocalUtil.save(mList);
                myAdapter.setData(mList);
            }

            @Override
            public void onDeleteClick(int position) {
                //删除item
                UrlEntity clickEntity = mList.get(position);
                for (int i = 0; i < mList.size(); i++) {
                    UrlEntity urlEntity = mList.get(i);
                    if (urlEntity.getScheme().equals(clickEntity.getScheme()) &&
                            urlEntity.getHost().equals(clickEntity.getHost()) &&
                            urlEntity.getPort() == clickEntity.getPort()) {
                        mList.remove(position);
                        LocalUtil.save(mList);
                        myAdapter.setData(mList);
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<UrlEntity> localList = LocalUtil.get();
        if (localList != null && localList.size() > 0) {
            mList = localList;
        }
        myAdapter.setData(mList);
    }

}
