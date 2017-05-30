package com.aurora.elezov.myapplication.Place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by USER on 18.02.2017.
 */

public interface PlaceAPI {
    @GET("api/place/nearbysearch/json?sensor=true&language=ru&key=AIzaSyAW-79sqdAygzJ_8cJz68oFne_qtePgJ-E")
    Call<Places> getNearbyPlaces(@Query("type") String type,
                                  @Query("location") String location,
                                  @Query("radius") int radius);

}
