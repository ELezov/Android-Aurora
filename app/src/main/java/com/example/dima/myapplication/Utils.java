package com.example.dima.myapplication;


import com.example.dima.myapplication.Details.ResultDetail;
import com.example.dima.myapplication.Place.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by USER on 18.02.2017.
 */

public class Utils {
    public static final String GOOGLE_API_KEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    public static final String BASE_URL = "https://maps.googleapis.com/maps/";
    public Places places;
    public ArrayList<LatLng> dirResults;
    public ResultDetail resultDetail;
    private static Utils utils;

    public ResultDetail getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(ResultDetail resultDetail) {
        this.resultDetail = resultDetail;
    }

    public ArrayList<LatLng> getDirResults() {
        return dirResults;
    }

    public void setDirResults(ArrayList<LatLng> dirResults) {

        this.dirResults = dirResults;
    }

    private Utils(){

    }

    public static Utils getInstance(){
        if(utils==null){
            utils=new Utils();
        }
        return utils;
    }

    public void setPlaces(Places ex){
        this.places=ex;
    }
    public Places getPlaces() {
        return places;
    }

    public String getApiKey(){
        return  GOOGLE_API_KEY;
    }

    public String getGetPlaceUrl(){
        return  BASE_URL;
    }
}
