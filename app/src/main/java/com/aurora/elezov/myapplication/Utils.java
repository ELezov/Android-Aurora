package com.aurora.elezov.myapplication;


import com.aurora.elezov.myapplication.Details.ResultDetail;
import com.aurora.elezov.myapplication.Direction.DirectionResults;
import com.aurora.elezov.myapplication.Place.Place;
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
    public List<Place> nearbyPlaces;
    public List<Place> selectPlaces;
    public List<Place> myTravel;
    public String EMAIL = "";

    public DirectionResults dirResultsInfo;
    public ArrayList<LatLng> dirResults;
    public ResultDetail resultDetail;
    private static Utils utils;

    public Place toTravel;
    public Place fromTravel;

    public Place getToTravel() {
        return toTravel;
    }

    public void setToTravel(Place toTravel) {
        this.toTravel = toTravel;
    }

    public Place getFromTravel() {
        return fromTravel;
    }

    public void setFromTravel(Place fromTravel) {
        this.fromTravel = fromTravel;
    }

    public List<Place> getMyTravel() {
        return myTravel;
    }

    public void setMyTravel(List<Place> myTravel) {
        this.myTravel = myTravel;
    }

    private Utils(){
        nearbyPlaces=new ArrayList<Place>();
        selectPlaces=new ArrayList<Place>();
        myTravel=new ArrayList<Place>();
    }

    public static Utils getInstance(){
        if(utils==null){
            utils=new Utils();
        }
        return utils;
    }

    public List<Place> getNearbyPlaces() {
        return nearbyPlaces;
    }

    public void setNearbyPlaces(List<Place> nearbyPlaces) {
        this.nearbyPlaces = nearbyPlaces;
    }

    public void addToNearbyPlaces(Place result){
        nearbyPlaces.add(result);
    }

    public List<Place> getSelectPlaces() {
        return selectPlaces;
    }

    public void setSelectPlaces(List<Place> selectPlaces) {
        this.selectPlaces = selectPlaces;
    }

    public void addToSelectPlaces(Place result){
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

    public void setCurrentEmail(String NewEmail) {
        this.EMAIL = NewEmail;
    }
    public String getCurrentEmail(){
        return  EMAIL;
    }


    public String getGetPlaceUrl(){
        return  BASE_URL;
    }

    public String getAuroraUrl(){ return  AURORA_URL; }

    public DirectionResults getDirResultsInfo() {
        return dirResultsInfo;
    }

    public void setDirResultsInfo(DirectionResults dirResultsInfo) {
        this.dirResultsInfo = dirResultsInfo;
    }


}
