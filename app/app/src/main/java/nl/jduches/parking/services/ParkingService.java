package nl.jduches.parking.services;


import java.util.List;

import nl.jduches.parking.domain.ParkingSpot;
import retrofit.http.GET;
import retrofit.http.Headers;
import rx.Observable;

public interface ParkingService {

    @GET("/api/spots/free")
    Observable<List<ParkingSpot>> getFreeSpots();


}
