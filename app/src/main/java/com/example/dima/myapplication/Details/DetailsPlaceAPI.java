package com.example.dima.myapplication.Details;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by USER on 19.02.2017.
 */

public interface DetailsPlaceAPI {
    @GET("api/place/details/json?language=ru&key=AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI")
    Call<ResultDetail> getPlaceDetails(@Query("placeid") String placeId);
}
