package com.aurora.elezov.myapplication.Social;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ASUS on 10.06.2017.
 */

public interface GetUserInfo {
    @GET("api/Users/{Id}")
    Call<GetUserID> getUserInfo(@Path("Id") String id);
}

