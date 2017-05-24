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
    public static final String AURORA_URL = "http://auroraproject.azurewebsites.net/";
    public List<Result> nearbyPlaces;
    public List<Result> selectPlaces;
    public List<Result> myTravel;


    public ArrayList<LatLng> dirResults;
    public ResultDetail resultDetail;
    private static Utils utils;

    public Result toTravel;
    public Result fromTravel;

    public Result getToTravel() {
        return toTravel;
    }

    public void setToTravel(Result toTravel) {
        this.toTravel = toTravel;
    }

    public Result getFromTravel() {
        return fromTravel;
    }

    public void setFromTravel(Result fromTravel) {
        this.fromTravel = fromTravel;
    }

    public List<Result> getMyTravel() {
        return myTravel;
    }

    public void setMyTravel(List<Result> myTravel) {
        this.myTravel = myTravel;
    }

    private Utils(){
        nearbyPlaces=new ArrayList<Result>();
        selectPlaces=new ArrayList<Result>();
        myTravel=new ArrayList<Result>();
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

    public void deleteFromSelectPlaces(int position)
    {
        selectPlaces.remove(position);
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

    public String getAuroraUrl(){ return  AURORA_URL; }
}
