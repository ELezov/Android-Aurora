package com.example.dima.myapplication;


import com.example.dima.myapplication.Details.ResultDetail;
import com.example.dima.myapplication.Place.Places;
import com.example.dima.myapplication.Place.Result;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 18.02.2017.
 */

public class Utils {
    public static final String GOOGLE_API_KEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    public static final String BASE_URL = "https://maps.googleapis.com/maps/";
    public List<Result> nearbyPlaces;
    public List<Result> selectPlaces;


    public ArrayList<LatLng> dirResults;
    public ResultDetail resultDetail;
    private static Utils utils;

    private Utils(){
        nearbyPlaces=new ArrayList<Result>();
        selectPlaces=new ArrayList<Result>();
    }

    public static Utils getInstance(){
        if(utils==null){
            utils=new Utils();
        }
        return utils;
    }

    public List<Result> getNearbyPlaces() {
        return nearbyPlaces;
    }

    public void setNearbyPlaces(List<Result> nearbyPlaces) {
        this.nearbyPlaces = nearbyPlaces;
    }

    public void addToNearbyPlaces(Result result){
        nearbyPlaces.add(result);
    }

    public List<Result> getSelectPlaces() {
        return selectPlaces;
    }

    public void setSelectPlaces(List<Result> selectPlaces) {
        this.selectPlaces = selectPlaces;
    }

    public void addToSelectPlaces(Result result){
        selectPlaces.add(result);
    }

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

    public String getApiKey(){
        return  GOOGLE_API_KEY;
    }

    public String getGetPlaceUrl(){
        return  BASE_URL;
    }
}
