package nl.jduches.parking.services;


import retrofit.http.GET;
import retrofit.http.Headers;
import rx.Observable;

public interface CheappService {

    @GET("link")
    Observable<Object> getResponse();

    //max-age in minutes
    @Headers("Cache-Control:max-age=120,only-if-cached,max-stale")
    @GET("link")
    Observable<Object> getCachedResponse();
}
