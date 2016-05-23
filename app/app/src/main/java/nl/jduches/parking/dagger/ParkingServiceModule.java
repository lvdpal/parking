package nl.jduches.parking.dagger;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.jduches.parking.services.ParkingService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module(includes = {CoreModule.class})
public class ParkingServiceModule {

    private Retrofit getAdapter(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                //TODO change url
                .baseUrl("http://10.0.3.2:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    ParkingService provideService(OkHttpClient client, Gson gson) {
        return getAdapter(client, gson).create(ParkingService.class);
    }
}
