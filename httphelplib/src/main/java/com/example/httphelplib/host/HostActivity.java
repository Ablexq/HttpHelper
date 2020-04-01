package com.example.httphelplib.host;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.httphelplib.R;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HostActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private HostAdapter myAdapter;
    private List<UrlEntity> mList = new ArrayList<>();
    private EditText etHost;
    private EditText etPort;
    private Button btnAdd;
    private EditText etScheme;
    private EditText etName;
    private Button btnScan;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        BGAQRCodeUtil.setDebug(true);

        initView();
        initRv();
    }

    private void initRv() {
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

    private void initView() {
        recyclerView = this.findViewById(R.id.rv);
        btnScan = findViewById(R.id.btn_scan);
        etName = findViewById(R.id.etName);
        etScheme = findViewById(R.id.etScheme);
        etHost = findViewById(R.id.etHost);
        etPort = findViewById(R.id.etPort);
        btnAdd = findViewById(R.id.btn_add);
        btnScan.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
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


    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int SCAN_REQUEST_CODE = 101;
    public static final int SCAN_RESULT_CODE = 201;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST_CODE &&
                resultCode == SCAN_RESULT_CODE) {
            if (data == null) {
                return;
            }

            String scan_result = data.getStringExtra("scan_result");
            System.out.println("scan_result==========================" + scan_result);
            //https://www.baidu.com/
            //https://www.jd.com/
            //https://www.1688.com/
            if (!TextUtils.isEmpty(scan_result) && scan_result.indexOf("://") > 0) {
                String[] array = scan_result.split("://");
                String scheme = array[0];
                String host = "";
                if (array[1].contains("/")) {
                    host = array[1].substring(0, array[1].indexOf("/"));
                } else {
                    host = array[1];
                }
                etHost.setText(host);
                etScheme.setText(scheme);

                if (scheme.contains("https")) {
                    etPort.setText("443");
                } else if (scheme.contains("http")) {
                    etPort.setText("80");
                }
            } else {
                ToastUtils.showShort("请输入正确的url,如：https://www.baidu.com/");
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_scan) {
            toScan();
        } else if (id == R.id.btn_add) {
            add();
        }
    }

    private void toScan() {
        Intent intent = new Intent(HostActivity.this, ScanActivity.class);
        startActivityForResult(intent, SCAN_REQUEST_CODE);

        etName.setText("");
        etScheme.setText("");
        etHost.setText("");
        etPort.setText("");
    }

    private void add() {
        String name = etName.getText().toString().trim();
        String scheme = etScheme.getText().toString().trim();
        String host = etHost.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort("名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(scheme)) {
            ToastUtils.showShort("scheme不能为空");
            return;
        }
        if (TextUtils.isEmpty(host)) {
            ToastUtils.showShort("host不能为空");
            return;
        }

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
}
