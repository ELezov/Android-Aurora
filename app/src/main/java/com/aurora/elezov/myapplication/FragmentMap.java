package com.aurora.elezov.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 19.02.2017.
 */

public class FragmentMap extends Fragment {

    View rootView;
    MapView mMapView;
    private GoogleMap googleMap;
    Utils utils;
    ArrayList<LatLng> routelist;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyLog", "onCreate");
        routelist=new ArrayList<LatLng>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("MyLog", "onCreateView");
        rootView=inflater.inflate(R.layout.fragment_first,container,false);

        utils=Utils.getInstance();
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.clear();

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                //Режим просмотра этажности
                // googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                //отключение быстрого доступа к оригиналу Google Maps
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                //Настройка параметров zoom
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng kant = new LatLng(54.706908, 20.512114);


                Log.d("MyLog", "utils.getExample()!=null");
                if(utils.getMyTravel().size()!=0)
                {
                    int count=utils.getMyTravel().size();

                    List<com.aurora.elezov.myapplication.Place.Result> myTravelPlaces=utils.getMyTravel();

                    for (int i=0;i<count;i++)
                    {
                        if (i==0) {
                            googleMap.addMarker(new MarkerOptions()
                                    .title(myTravelPlaces.get(i).getName()
                                    )
                                    .position(
                                            new LatLng(myTravelPlaces.get(i).getGeometry().getLocation().getLat(),
                                                    myTravelPlaces.get(i).getGeometry().getLocation().getLng()))
                                    .snippet(myTravelPlaces.get(i).getVicinity())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            );
                        }
                        else
                        if (i==1) {
                            googleMap.addMarker(new MarkerOptions()
                                    .title(myTravelPlaces.get(i).getName()
                                    )
                                    .position(
                                            new LatLng(myTravelPlaces.get(i).getGeometry().getLocation().getLat(),
                                                    myTravelPlaces.get(i).getGeometry().getLocation().getLng()))
                                    .snippet(myTravelPlaces.get(i).getVicinity())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            );
                        }
                        else {
                            googleMap.addMarker(new MarkerOptions()
                                    .title(myTravelPlaces.get(i).getName()
                                    )
                                    .position(
                                            new LatLng(myTravelPlaces.get(i).getGeometry().getLocation().getLat(),
                                                    myTravelPlaces.get(i).getGeometry().getLocation().getLng()))
                                    .snippet(myTravelPlaces.get(i).getVicinity())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            );
                        }
                    }
                }


                if(utils.getDirResults()!=null) {
                    Log.v("COUNT Direction",""+utils.getDirResults().size());
                    routelist = utils.getDirResults();
                    if (routelist.size() > 0) {
                        PolylineOptions rectLine = new PolylineOptions().width(10).color(
                                Color.RED);

                        for (int i = 0; i < routelist.size(); i++) {
                            rectLine.add(routelist.get(i));
                        }
                        // Adding route on the map
                        mMap.addPolyline(rectLine);
                        //markerOptions.position(toPosition);
                        //markerOptions.draggable(true);
                        //mMap.addMarker(markerOptions);
                    }
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(kant, 12);
                googleMap.animateCamera(cameraUpdate);

            }
        });


        return rootView;
    }
}
