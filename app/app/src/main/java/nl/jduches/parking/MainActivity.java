package nl.jduches.parking;

import android.os.Bundle;

public class MainActivity extends AbstractActivity {

    @Override
    public void inject(CoreApplication.ApplicationComponent injector) {
        injector.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}