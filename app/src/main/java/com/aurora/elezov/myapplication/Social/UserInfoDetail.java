package com.aurora.elezov.myapplication.Social;

import com.aurora.elezov.myapplication.Details.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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