package com.example.mysample.okhttp;


/**
 * Created by Cg on 2017/6/1.
 */
public interface OkHttpUploadListener {

    void onUploadProgress(long current, long total, boolean done);

}
