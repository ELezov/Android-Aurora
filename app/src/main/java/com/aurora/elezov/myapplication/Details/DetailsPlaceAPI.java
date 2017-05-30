package com.aurora.elezov.myapplication.Details;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by USER on 19.02.2017.
 */

public interface DetailsPlaceAPI {
    @GET("api/place/details/json?language=ru&key=AIzaSyAW-79sqdAygzJ_8cJz68oFne_qtePgJ-E")
    Call<ResultDetail> getPlaceDetails(@Query("placeid") String placeId);
}
