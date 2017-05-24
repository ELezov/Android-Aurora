package com.example.dima.myapplication.Social;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Query;

/**
 * Created by ASUS on 17.03.2017.
 */

public interface UserInfoAPI {
    @FormUrlEncoded
    @POST("api/Users")
        //Call<UserInfoDetail> PostUserInfo(@Field("mail") String EMAIL);
    Call<UserInfoDetail> PostUserInfo(@Field("id") int SOMEID, @Field("mail") String EMAIL);
    //Call<> loadRepo();
}
