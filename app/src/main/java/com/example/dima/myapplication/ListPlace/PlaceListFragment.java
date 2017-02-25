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


public class PlaceListFragment extends Fragment {

    LocationManager locationManager;
    Double longitude;
    Double latitude;
    Location loc;
    Utils utils;
    RecyclerView rv;
    List<String> arr;
    ProgressDialog progressDialog;

    RecyclerViewNearbyPlacesAdapter MyAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_nearby_places_list, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait!");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


       /* if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return Fragment.
                    requestPermissions(getContext(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },1);
        }*/

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
        }
        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (loc==null)
            loc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (loc.getProvider().equals(LocationManager.NETWORK_PROVIDER) || loc.getProvider().equals(locationManager.GPS_PROVIDER)) {
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
            }

            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            String type = "art_gallery|city_hall|museum|park|zoo";
            utils = Utils.getInstance();

            RecyclerViewNearbyPlacesAdapter adapter = new RecyclerViewNearbyPlacesAdapter(getActivity());
            MyAdapter = new RecyclerViewNearbyPlacesAdapter(getActivity());

            build_retrofit_and_get_response(type);

        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(getContext(),"Включите GPS",Toast.LENGTH_LONG).show();
        }

        return rv;
    }


    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlaceAPI service = retrofit.create(PlaceAPI.class);

        Call<Places> call = service.getNearbyPlaces(type, latitude + "," + longitude, 10000);

        call.enqueue(new Callback<Places>() {
            @Override
            public void onResponse(Call<Places> call, Response<Places> response) {
                utils.setNearbyPlaces(response.body().getResults());

                /*List<String> arr=new ArrayList<String>();
                for (int i = 0; i < response.body().getResults().size(); i++) {
                    arr.add(i,response.body().getResults().get(i).getName());

                }*/

                List<Result> arr=new ArrayList<Result>();
                arr=utils.getNearbyPlaces();
                MyAdapter.add(arr);
                rv.setAdapter(MyAdapter);
                //setupRecyclerView(rv);
                build_retrofit_and_get_duration_in_response();
            }

            @Override
            public void onFailure(Call<Places> call, Throwable t) {

            }

        });
    }

    private void build_retrofit_and_get_duration_in_response() {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DirectionAPI service = retrofit.create(DirectionAPI.class);

        int size=utils.getNearbyPlaces().size();
        Double lat1 = utils.getNearbyPlaces().get(0).getGeometry().getLocation().getLat();
        Double lon1 = utils.getNearbyPlaces().get(0).getGeometry().getLocation().getLng();

        Double lat2 = utils.getNearbyPlaces().get(1).getGeometry().getLocation().getLat();
        Double lon2 = utils.getNearbyPlaces().get(1).getGeometry().getLocation().getLng();
        String waypoint="";
        if (utils.getNearbyPlaces().size()>2)
        {
            waypoint = "optimize:true|";
            int max=0;
            if (size>10){
                size=10;
            }
            for (int i=2;i<size;i++)
            {
                if (i==size-1)
                {
                    waypoint+=utils.getNearbyPlaces().get(i).getGeometry().getLocation().getLat().toString()+
                            ","+utils.getNearbyPlaces().get(i).getGeometry().getLocation().getLng().toString();
                }
                else {
                    waypoint+=utils.getNearbyPlaces().get(i).getGeometry().getLocation().getLat().toString()+
                            ","+utils.getNearbyPlaces().get(i).getGeometry().getLocation().getLng().toString()+"|";
                }


             }
         }

        Call<DirectionResults> call = service.getJson(lat1.toString() + "," + lon1.toString(), lat2.toString() + "," + lon2.toString(),waypoint);

        call.enqueue(new Callback<DirectionResults>(){

            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
                DirectionResults directionResults=response.body();
                progressDialog.dismiss();
                ArrayList<LatLng> routelist = new ArrayList<LatLng>();
                if(directionResults.getRoutes().size()>0) {
                    ArrayList<LatLng> decodelist;
                    Route routeA = directionResults.getRoutes().get(0);
                    if (routeA.getLegs().size() > 0) {
                        for (int k=0; k<routeA.getLegs().size();k++) {
                            List<Steps> steps = routeA.getLegs().get(k).getSteps();
                            Steps step;
                            com.example.dima.myapplication.Direction.Location location;
                            String polyline;
                            for (int i = 0; i < steps.size(); i++) {
                                step = steps.get(i);
                                location = step.getStart_location();
                                //Log.v("1111", step.getStart_location().getLat() + "," + step.getStart_location().getLng());
                                routelist.add(new LatLng(location.getLat(), location.getLng()));
                                polyline = step.getPolyline().getPoints();
                                decodelist = RouteDecode.decodePoly(polyline);
                                routelist.addAll(decodelist);
                                location = step.getEnd_location();
                                //Log.v("2222", step.getEnd_location().getLat() + "," + step.getEnd_location().getLng());
                                routelist.add(new LatLng(location.getLat(), location.getLng()));
                            }
                        }
                    }
                }
                utils.setDirResults(routelist);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {

            }
        });
    }


    /*private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }*/



}
