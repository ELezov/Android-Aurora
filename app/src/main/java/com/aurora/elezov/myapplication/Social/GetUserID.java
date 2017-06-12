package com.aurora.elezov.myapplication.Social;

import com.aurora.elezov.myapplication.Details.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 10.06.2017.
 */

public class GetUserID {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mail")
    @Expose
    private String mail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
