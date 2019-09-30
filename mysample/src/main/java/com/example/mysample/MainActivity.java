package com.example.mysample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.mysample.okhttp.OkHttpUploadListener;
import com.example.mysample.okhttp.OkhttpUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "cj";
    private Button bt1;
    private Button bt2;
    private Button bt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:

                OkhttpUtils.getInstance().doGet("http://192.168.137.1:8090", null, new OkhttpUtils.ResponseListener() {
                    @Override
                    public void success(String str) {
                        Log.e(TAG, "success: " + str);
                    }

                    @Override
                    public void fail(Call call, IOException e) {
                        Log.e(TAG, "fail: " + e.getMessage());
                    }
                });

                break;
            case R.id.bt2:

                break;
            case R.id.bt3:
                //upload
                OkhttpUtils.getInstance().uploadFile("http://192.168.137.1:8090", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hw.apk", new OkhttpUtils.ResponseListener() {
                    @Override
                    public void success(String str) {

                    }

                    @Override
                    public void fail(Call call, IOException e) {

                    }
                });

                break;

            case R.id.bt4:

                OkhttpUtils.getInstance().uploadFileWithProgress("http://192.168.137.1:8090", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hw.apk", new OkhttpUtils.ResponseListener() {
                    @Override
                    public void success(String str) {

                    }

                    @Override
                    public void fail(Call call, IOException e) {

                    }
                }, new OkHttpUploadListener() {
                    @Override
                    public void onUploadProgress(long current, long total, boolean done) {
                        Log.e(TAG, "onUploadProgress: " + (current * 100 / total));
                    }
                });

                break;
        }
    }
}
