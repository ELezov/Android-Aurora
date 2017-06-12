package com.aurora.elezov.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurora.elezov.myapplication.Direction.DirectionResults;
import com.aurora.elezov.myapplication.Place.Place;
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
        routelist = new ArrayList<LatLng>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("MyLog", "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_first, container, false);

        utils = Utils.getInstance();
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


                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
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

                    List<com.aurora.elezov.myapplication.Place.Place> myTravelPlaces=utils.getMyTravel();

                    int i=0;
                    Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.start);
                    Bitmap bitmap = bitmapSizeByScall(icon, 0.3f);
                    googleMap.addMarker(new MarkerOptions()
                            .title(myTravelPlaces.get(i).getName())
                            .position(new LatLng(myTravelPlaces.get(i).getGeometry().getLocation().getLat(),
                                    myTravelPlaces.get(i).getGeometry().getLocation().getLng()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .snippet(myTravelPlaces.get(i).getVicinity()));

                    i=1;
                    icon = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.finish);
                    bitmap = bitmapSizeByScall(icon, 0.3f);
                    googleMap.addMarker(new MarkerOptions()
                            .title(myTravelPlaces.get(i).getName())
                            .position(new LatLng(myTravelPlaces.get(i).getGeometry().getLocation().getLat(),
                                    myTravelPlaces.get(i).getGeometry().getLocation().getLng()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .snippet(myTravelPlaces.get(i).getVicinity()));

                    if (utils.getDirResults() != null) {
                        DirectionResults result = utils.getDirResultsInfo();
                        List<Integer> waypointOrder = result.getRoutes().get(0).getWaypointOrder();
                        int size = waypointOrder.size();
                        for (int j = 0; j < size; j++) {
                            int number = waypointOrder.get(j);
                            String name = "marker_" + (j + 1);
                            Log.d("Marker", name);
                            Drawable drawable = getResources().getDrawable(getResources()
                                    .getIdentifier(name, "drawable", getContext().getPackageName()));
                            icon = drawableToBitmap(drawable);
                            bitmap = bitmapSizeByScall(icon, 0.3f);

                            googleMap.addMarker(new MarkerOptions()
                                    .title(myTravelPlaces.get(number + 2).getName()
                                    )
                                    .position(
                                            new LatLng(myTravelPlaces.get(number+2).getGeometry().getLocation().getLat(),
                                                    myTravelPlaces.get(number+2).getGeometry().getLocation().getLng()))
                                    .snippet(myTravelPlaces.get(number+2).getVicinity())
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                        }

                    }


                    /*for (i=2;i<count;i++)
                            googleMap.addMarker(new MarkerOptions()
                                    .title(myTravelPlaces.get(i).getName()
                                    )
                                    .position(
                                            new LatLng(myTravelPlaces.get(i).getGeometry().getLocation().getLat(),
                                                    myTravelPlaces.get(i).getGeometry().getLocation().getLng()))
                                    .snippet(myTravelPlaces.get(i).getVicinity())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            );*/
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
                        mMap.addPolyline(rectLine);
                    }
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(kant, 12);
                googleMap.animateCamera(cameraUpdate);

            }
        });



        return rootView;
    }

    public Bitmap bitmapSizeByScall( Bitmap bitmapIn, float scall_zero_to_one_f) {

        Bitmap bitmapOut = Bitmap.createScaledBitmap(bitmapIn,
                Math.round(bitmapIn.getWidth() * scall_zero_to_one_f),
                Math.round(bitmapIn.getHeight() * scall_zero_to_one_f), false);

        return bitmapOut;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}


