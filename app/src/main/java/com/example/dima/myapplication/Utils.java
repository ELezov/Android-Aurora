package com.example.dima.myapplication;

import com.example.dima.myapplication.Details.ResultDetail;
import com.example.dima.myapplication.Direction.DirectionResults;
import com.example.dima.myapplication.Place.Place;
import com.example.dima.myapplication.Retrofit.Example;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by USER on 18.02.2017.
 */

public class Utils {
    public static final String GOOGLE_API_KEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    public static final String GET_PLACE_URL = "https://maps.googleapis.com/maps/";
    public Example example;
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


    public void setExample(Example ex){
        this.example=ex;
    }
    public Example getExample() {
        return example;
    }

    public String getApiKey(){
        return  GOOGLE_API_KEY;
    }

    public String getGetPlaceUrl(){
        return  GET_PLACE_URL;
    }
}
