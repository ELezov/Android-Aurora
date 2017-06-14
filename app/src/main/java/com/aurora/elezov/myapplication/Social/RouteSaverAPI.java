package com.aurora.elezov.myapplication.Social;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ASUS on 05.06.2017.
 */

public interface RouteSaverAPI {

   @FormUrlEncoded
   @POST("api/Routes")
   Call<ResponseBody> RouteSaverDetail(@Field("id") int SOMEID, @Field("route") String EMAIL, @Field("user_id") int USERID);
}
