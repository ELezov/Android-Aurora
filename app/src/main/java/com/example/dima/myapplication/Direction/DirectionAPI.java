package com.example.dima.myapplication.Direction;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by USER on 19.02.2017.
 */

public interface DirectionAPI {
    @GET("api/directions/json?key=AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI")
    Call<DirectionResults> getJson(@Query("origin") String origin,
                                   @Query("destination") String destination,
                                   @Query("waypoints") String waypoints);
}

