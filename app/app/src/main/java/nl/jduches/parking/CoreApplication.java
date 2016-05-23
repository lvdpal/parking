package nl.jduches.parking;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import nl.jduches.parking.dagger.ParkingServiceModule;
import nl.jduches.parking.dagger.CoreModule;
import nl.jduches.parking.fragments.ParkingListFragment;

public class CoreApplication extends Application {

    @Singleton
    @Component(modules = {CoreModule.class, ParkingServiceModule.class})
    public interface ApplicationComponent {
        void inject(CoreApplication application);
        void inject(MainActivity activity);
        void inject(ParkingListFragment parkingListFragment);
    }

    private ApplicationComponent component;

    @Inject
    OkHttpClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerCoreApplication_ApplicationComponent.builder()
                .coreModule(new CoreModule(this))
                .parkingServiceModule(new ParkingServiceModule())
                .build();

        component.inject(this);
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }

    public ApplicationComponent component(){
        return component;
    }
}
