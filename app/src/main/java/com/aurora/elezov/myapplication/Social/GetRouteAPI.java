package com.aurora.elezov.myapplication.Social;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ASUS on 09.06.2017.
 */

public interface GetRouteAPI {
    @GET("api/Routes")
    Call<RoutesResult> getRoute();
}
