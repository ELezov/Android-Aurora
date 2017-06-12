package com.aurora.elezov.myapplication.Direction;


/**
 * Created by USER on 19.02.2017.
 */

public class Steps {
    private Location start_location;
    private Location end_location;
    private OverviewPolyLine polyline;
    private String html_instructions;


    public Location getStart_location() {
        return start_location;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public OverviewPolyLine getPolyline() {
        return polyline;
    }

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }
}
