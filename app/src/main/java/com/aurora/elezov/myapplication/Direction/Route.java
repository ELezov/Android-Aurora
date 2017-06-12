package com.aurora.elezov.myapplication.Direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 19.02.2017.
 */

public class Route {
    @SerializedName("overview_polyline")
    private OverviewPolyLine overviewPolyLine;

    @SerializedName("waypoint_order")
    private List<Integer> waypointOrder = null;

    private List<Legs> legs;

    public OverviewPolyLine getOverviewPolyLine() {
        return overviewPolyLine;
    }

    public List<Legs> getLegs() {
        return legs;
    }

    public List<Integer> getWaypointOrder() {
        return waypointOrder;
    }

    public void setWaypointOrder(List<Integer> waypointOrder) {
        this.waypointOrder = waypointOrder;
    }
}
