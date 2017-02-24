package com.example.dima.myapplication.Place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by USER on 18.02.2017.
 */

public interface PlaceAPI {
    @GET("api/place/nearbysearch/json?sensor=true&language=ru&key=AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI")
    Call<Places> getNearbyPlaces(@Query("type") String type,
                                  @Query("location") String location,
                                  @Query("radius") int radius);

}
