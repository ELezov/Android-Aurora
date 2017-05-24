package com.example.dima.myapplication.Social;

import com.example.dima.myapplication.Details.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ASUS on 17.03.2017.
 */

public class UserInfoDetail {

    @SerializedName("result")
    @Expose
    private Result result;


    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


}