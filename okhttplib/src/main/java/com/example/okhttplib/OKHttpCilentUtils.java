package com.example.okhttplib;

import android.content.Context;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * @author zcj
 * @date 2019/7/17
 */
public class OKHttpCilentUtils {
    OkHttpClient client;


    public OkHttpClient getClient() {
        return client;

    }

    /**
     * 按tag取消请求.
     *
     * @param context
     */
    public void cancel(Context context) {
        if (context == null) {
            return;
        }
        synchronized (getClient().dispatcher().getClass()) {
            for (Call call : getClient().dispatcher().queuedCalls()) {
                if (context.equals(call.request().tag())) {

                    call.cancel();
                }
            }

            for (Call call : getClient().dispatcher().runningCalls()) {
                if (context.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }
}
