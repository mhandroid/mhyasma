package com.android.mh.yasma.utils;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by @author Mubarak Hussain.
 */
public class CachIntercepter implements Interceptor {
    private Context mContext;

    public CachIntercepter(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (Utils.isNetworkAvailable(mContext)) {
            request.newBuilder().header("Cache-Control", "public, max-age=" + 10).build();
        } else {
            request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 20).build();

        }

        return chain.proceed(request);
    }
}
