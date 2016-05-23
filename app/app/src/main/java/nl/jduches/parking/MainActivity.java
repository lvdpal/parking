package nl.jduches.parking;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nl.jduches.parking.domain.ParkingSpot;
import nl.jduches.parking.services.ParkingService;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AbstractActivity {

    @InjectView(R.id.button_ok)
    Button buttonOk;

    @InjectView(R.id.text_hello)
    TextView textHello;

    @Inject
    ParkingService parkingService;

    @Override
    public void inject(CoreApplication.ApplicationComponent injector) {
        injector.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make reservation as soon as API works
            }
        });

        parkingService.getFreeSpots().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<ParkingSpot>>() {
            @Override
            public void call(List<ParkingSpot> parkingSpots) {
                // put output in text field
                for (ParkingSpot spot : parkingSpots) {
                    textHello.append(spot.getNumber()+" ");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("Main", throwable.getMessage());// do nothing
            }
        });
    }


}