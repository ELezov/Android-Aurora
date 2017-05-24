package com.example.dima.myapplication.ListPlace;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.dima.myapplication.Direction.DirectionAPI;
import com.example.dima.myapplication.Direction.DirectionResults;
import com.example.dima.myapplication.Direction.Route;
import com.example.dima.myapplication.Direction.RouteDecode;
import com.example.dima.myapplication.Direction.Steps;
import com.example.dima.myapplication.Place.Places;
import com.example.dima.myapplication.Place.Result;
import com.example.dima.myapplication.R;
import com.example.dima.myapplication.Place.PlaceAPI;
import com.example.dima.myapplication.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NearbyPlacesFragment extends Fragment {


    Utils utils;
    RecyclerView rv;
    public static RecyclerViewNearbyPlacesAdapter MyAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_nearby_places_list, container, false);


        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        utils = Utils.getInstance();

        RecyclerViewNearbyPlacesAdapter adapter = new RecyclerViewNearbyPlacesAdapter();
        MyAdapter = new RecyclerViewNearbyPlacesAdapter();
        List<Result> arr=new ArrayList<Result>();
        arr=utils.getNearbyPlaces();
        MyAdapter.add(arr);
        rv.setAdapter(MyAdapter);
        return rv;
    }







}
