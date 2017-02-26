package com.example.dima.myapplication.ListPlace;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dima.myapplication.R;
import com.example.dima.myapplication.Utils;

import java.util.List;


public class PlaceListFragment2 extends Fragment {

    LocationManager locationManager;
    RecyclerView rv;
    List<String> arr;
    Utils utils;
    Double longitude;
    Double latitude;

   public RecyclerViewSelectPlacesAdapter MyAdapter;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_select_places_list, container, false);
        utils=Utils.getInstance();
        RecyclerViewSelectPlacesAdapter adapter = new RecyclerViewSelectPlacesAdapter();
        MyAdapter = adapter;
        MyAdapter.add(utils.getSelectPlaces());
        MyAdapter.notifyDataSetChanged();
        rv.setAdapter(MyAdapter);


        return rv;
    }





}