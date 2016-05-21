package nl.jduches.parking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(((CoreApplication) getApplication()).component());
    }

    public abstract void inject(CoreApplication.ApplicationComponent injector);
}
