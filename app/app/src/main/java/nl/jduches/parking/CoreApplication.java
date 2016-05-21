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
import nl.jduches.parking.dagger.CheappServiceModule;
import nl.jduches.parking.dagger.CoreModule;

public class CoreApplication extends Application {

    @Singleton
    @Component(modules = {CoreModule.class, CheappServiceModule.class})
    public interface ApplicationComponent {
        void inject(CoreApplication application);
        void inject(MainActivity activity);
    }

    private ApplicationComponent component;

    @Inject
    OkHttpClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerCoreApplication_ApplicationComponent.builder()
                .coreModule(new CoreModule(this))
                .cheappServiceModule(new CheappServiceModule())
                .build();

        component.inject(this);
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }

    public ApplicationComponent component(){
        return component;
    }
}
