package com.aurora.elezov.myapplication.ListPlace;

import android.app.ProgressDialog;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurora.elezov.myapplication.R;
import com.aurora.elezov.myapplication.Utils;

import java.util.List;


public class MyPlacesFragment extends Fragment {

    LocationManager locationManager;
    RecyclerView rv;
    List<String> arr;
    Utils utils;
    Double longitude;
    Double latitude;
    private RecyclerView.LayoutManager mLayoutManager;

   public static RecyclerViewSelectPlacesAdapter MyAdapter;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        utils=Utils.getInstance();
        rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_select_places_list, container, false);
        mLayoutManager=new LinearLayoutManager(getContext());
        Log.v("Select Count",""+utils.getSelectPlaces().size());
        rv.setLayoutManager(mLayoutManager);
        RecyclerViewSelectPlacesAdapter adapter = new RecyclerViewSelectPlacesAdapter();
        MyAdapter = adapter;
        MyAdapter.add(utils.getSelectPlaces());
        Log.v("Adapter Count",""+MyAdapter.getItemCount());

        MyAdapter.notifyDataSetChanged();
        rv.setAdapter(MyAdapter);


        return rv;
    }





}