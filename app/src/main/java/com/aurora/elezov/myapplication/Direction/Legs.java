package com.aurora.elezov.myapplication.Direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 19.02.2017.
 */

public class Legs {
    private List<Steps> steps;
    @SerializedName("distance")
    @Expose
    private Distance distance;
    @SerializedName("duration")
    @Expose
    private Duration duration;

    public List<Steps> getSteps() {
        return steps;
    }

}
