package com.example.mysample.okhttp;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cj on 2017/9/20.
 */

public class OkhttpUtils {

    private static final String TAG = "cj";
    private OkHttpClient mHttpClient;
    private static OkhttpUtils instance;


    private OkhttpUtils() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static OkhttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkhttpUtils.class) {
                if (instance == null) {
                    instance = new OkhttpUtils();
                }
            }
        }
        return instance;
    }

    public void doPost(String url, Map<String, String> body, final ResponseListener responseListener) {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<Map.Entry<String, String>> iterator = body.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.fail(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                responseListener.success(str);
            }

        });
    }

    public void doGet(String url, Map<String, String> head, final ResponseListener responseListener) {

        Request.Builder requestBuilder = new Request.Builder();

        requestBuilder.url(url);
        //可以省略，默认是GET请求
        requestBuilder.method("GET", null);
        //有头
        if (head != null) {
            for (Map.Entry<String, String> entry : head.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                requestBuilder.addHeader(key, value);
            }
        }

        Request request = requestBuilder.build();
        Log.e(TAG, "doget request: " + request.toString());
        Call mcall = mHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                responseListener.fail(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                } else {
                    String str = response.body().string();
                    responseListener.success(str);
                }
            }
        });
    }

    public void uploadFile(String url, String filePath, final ResponseListener responseListener) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath));
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "上传失败 == " + e.getMessage());
                responseListener.fail(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseListener.success("上传成功");

                Log.e(TAG, "上传成功: " + response.toString());
            }
        });

    }

    /**
     * 带进度上传
     */
    public void uploadFileWithProgress(String url, String filePath, final ResponseListener responseListener, OkHttpUploadListener uploadListener) {
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath));

        RequestBody requestBody = new OkHttpRequestBody(requestBody1, uploadListener);
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "上传失败 == " + e.getMessage());
                responseListener.fail(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseListener.success("上传成功");

                Log.e(TAG, "上传成功: " + response.toString());
            }
        });
    }


    public interface ResponseListener {
        void success(String str);

        void fail(Call call, IOException e);
    }

    private ResponseListener responseListener;

}
