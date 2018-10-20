package com.android.mh.yasma.api;

import android.content.Context;

import com.android.mh.yasma.utils.CachIntercepter;
import com.android.mh.yasma.utils.ConnectivityInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class to responsible to create api client for retrofit.
 */
public class ApiClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static Retrofit retrofit = null;

    private ApiClient() {
    }

    /**
     * to get the retrofit client object
     *
     * @return Retrofit
     */
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(context))
                    .addInterceptor(httpLoggingInterceptor)
                    .addNetworkInterceptor(new CachIntercepter())
                    .cache(getCach(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }

        return retrofit;
    }

    private static Cache getCach(Context context) {
        long cachSize = (5 * 1024 * 1024);
        return new Cache(context.getCacheDir(), cachSize);
    }


}
