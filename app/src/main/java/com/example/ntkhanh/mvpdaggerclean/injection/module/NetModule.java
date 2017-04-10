package com.example.ntkhanh.mvpdaggerclean.injection.module;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.ntkhanh.mvpdaggerclean.BuildConfig;
import com.example.ntkhanh.mvpdaggerclean.MyApplication;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ntkhanh on 3/19/17.
 */

@Module
public class NetModule {

    @Inject
    public NetModule() {
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(MyApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
    @Provides
    @Singleton
    Cache provideHttpCache(MyApplication application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BuildConfig.baseURL)
                .client(okHttpClient)
                .build();
        return retrofit;
    }
}
