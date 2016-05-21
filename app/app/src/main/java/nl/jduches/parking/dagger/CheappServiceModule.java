package nl.jduches.parking.dagger;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.jduches.parking.services.CheappService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module(includes = {CoreModule.class})
public class CheappServiceModule {

    private Retrofit getAdapter(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                //TODO change url
                .baseUrl("http://url.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    CheappService provideService(OkHttpClient client, Gson gson) {
        return getAdapter(client, gson).create(CheappService.class);
    }
}
