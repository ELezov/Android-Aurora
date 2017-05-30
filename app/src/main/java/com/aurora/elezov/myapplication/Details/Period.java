package com.aurora.elezov.myapplication.Details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 19.02.2017.
 */

public class Period {
    @SerializedName("close")
    @Expose
    private Close close;
    @SerializedName("open")
    @Expose
    private Open open;

    public Close getClose() {
        return close;
    }

    public void setClose(Close close) {
        this.close = close;
    }

    public Open getOpen() {
        return open;
    }

    public void setOpen(Open open) {
        this.open = open;
    }
}
