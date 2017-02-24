package com.example.dima.myapplication.Details;

import com.example.dima.myapplication.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by USER on 19.02.2017.
 */

public class DetailsLoad {
    String placeID;

    public DetailsLoad(String placeID){
        this.placeID=placeID;
    }

    public void  getDetailsData()
    {
        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DetailsPlaceAPI service = retrofit.create(DetailsPlaceAPI.class);

        Call<ResultDetail> call = service.getPlaceDetails(placeID);

        call.enqueue(new Callback<ResultDetail>() {
            @Override
            public void onResponse(Call<ResultDetail> call, Response<ResultDetail> response) {
                Utils utils=Utils.getInstance();

            }

            @Override
            public void onFailure(Call<ResultDetail> call, Throwable t) {

            }
        });

    }
}
