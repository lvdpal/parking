package nl.jduches.parking.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.byteunits.DecimalByteUnit;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.jduches.parking.CoreApplication;

@Module
public class CoreModule {

    private final CoreApplication application;

    public CoreModule(CoreApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return application.getSharedPreferences("Cheapp", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);

        File cacheDir = new File(application.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, (int) DecimalByteUnit.MEGABYTES.toBytes(50));
        client.setCache(cache);

        return client;
    }
}
