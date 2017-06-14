package com.aurora.elezov.myapplication.Place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by USER on 18.02.2017.
 */

public interface PlaceAPI {
    @GET("api/place/nearbysearch/json?sensor=true&language=ru&key=AIzaSyAW-79sqdAygzJ_8cJz68oFne_qtePgJ-E")
    Call<PlacesResult> getNearbyPlaces(@Query("type") String type,
                                       @Query("pagetoken") String pageToken,
                                       @Query("location") String location,
                                       @Query("rankby") String rankby,
                                       @Query("radius") String radius);

}
