package com.android.mh.yasma.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by @author Mubarak Hussain.
 */
public class CachIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(request);
        String cacheControl = originalResponse.header("Cache-Control");

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")) {

            CacheControl cc = new CacheControl.Builder()
                    .maxStale(1, TimeUnit.MINUTES)
                    .build();
            request = request.newBuilder()
                    .cacheControl(cc)
                    .build();

            return chain.proceed(request);

        } else {
            return originalResponse;
        }
    }
}
