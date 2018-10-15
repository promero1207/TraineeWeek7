package com.app.paul.galleryapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Retrofit client class
 */
public class RetrofitClient {
    private static final String CACHE_CONTROL = "Cache-Control";
    private static NasaApi nasaApiInstance;

    //Returns an api instance
    public static NasaApi getInstance() {
        if (nasaApiInstance == null) {
            String BASE_URI = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient())
                    .build();
            nasaApiInstance = retrofit.create(NasaApi.class);

        }

        return nasaApiInstance;
    }

    //Method that returns OkHttp client
    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideOfflineCache())
                .addInterceptor(provideHttpLoggingInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build();
    }

    //Method for providing cache to OkHttp
    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(MyApplication.getInstance().getCacheDir(), "http-cache"),
                    20 * 1024 * 1024); // 20 MB
        } catch (Exception e) {
            Log.d(MyApplication.getInstance().getPackageName() + ": ", e.getMessage());
        }
        return cache;
    }


    //Method for checking network state
    private static boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    //Http logging interceptor
    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(@NonNull String message) {

                    }
                });
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HEADERS : NONE);
        return httpLoggingInterceptor;
    }

    //Method for providing cache interceptor to OkHttp
    private static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    //Method for providing offline cache interceptor
    private static Interceptor provideOfflineCache() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();

                if (!checkIfHasNetwork()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS).build();
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }
}
