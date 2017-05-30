package com.aurora.elezov.myapplication.ListPlace;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aurora.elezov.myapplication.Place.Result;
import com.aurora.elezov.myapplication.R;
import com.aurora.elezov.myapplication.Utils;

import java.util.ArrayList;
import java.util.List;


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
