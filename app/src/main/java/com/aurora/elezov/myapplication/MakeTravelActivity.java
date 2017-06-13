package com.aurora.elezov.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.aurora.elezov.myapplication.Place.Geometry;
import com.aurora.elezov.myapplication.Place.Place;

import java.util.ArrayList;
import java.util.List;

public class MakeTravelActivity extends AppCompatActivity {

    Utils utils;
    SpinnerAdapter spinnerAdapterFrom;
    SpinnerAdapter spinnerAdapterTo;
    Place fromResult;
    Place toResult;

    LocationManager locationManager;
    Double longitude;
    Double latitude;
    Location loc;
    Place myLocation;

    List<Place> data;

    Button nextBtn;

    public static String fromTravel="FROM_TRAVEL";
    public static String toTravel="TO_TRAVEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_travel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        nextBtn=(Button)findViewById(R.id.nextBtn);

        final Spinner spinnerFrom = (Spinner) findViewById(R.id.spinnerTravelFrom);
        final Spinner spinnerTo = (Spinner) findViewById(R.id.spinnerTravelTo);

        utils = Utils.getInstance();
        addMyLocationToTravel();

        data = new ArrayList<Place>();
        if (loc != null)
        {
            myLocation=new Place();
            myLocation.setName("Моё текущее местоположение");
            myLocation.setPlaceId("111");
            Log.v("LOG",myLocation.getPlaceId());
            Geometry geometry=new Geometry();
            com.aurora.elezov.myapplication.Place.Location location=new com.aurora.elezov.myapplication.Place.Location();
            location.setLng(longitude);
            location.setLat(latitude);
            geometry.setLocation(location);
            myLocation.setGeometry(geometry);
            Log.v("LOG",""+myLocation.getName());
            data.add(myLocation);
         }
        for (int i=0;i<utils.getSelectPlaces().size();i++)
        {
            data.add(data.size(),utils.getSelectPlaces().get(data.size()-1));
        }
        Log.v("LOG",""+data.size());

        spinnerAdapterFrom =new SpinnerAdapter(this,R.layout.support_simple_spinner_dropdown_item,data);
        spinnerAdapterTo=new SpinnerAdapter(this,R.layout.support_simple_spinner_dropdown_item,data);
        spinnerAdapterFrom.notifyDataSetChanged();
        spinnerAdapterTo.notifyDataSetChanged();
        spinnerFrom.setAdapter(spinnerAdapterFrom);
        spinnerTo.setAdapter(spinnerAdapterTo);
        spinnerFrom.setSelection(-1);
        spinnerTo.setSelection(-1);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                fromResult=spinnerAdapterFrom.getItem(position);
                if (spinnerAdapterFrom.getFlag().equals(true))
                {
                    Toast.makeText(getApplicationContext(),"Концом маршрута выбрано " + toResult.getName(),Toast.LENGTH_SHORT).show();
                }
                spinnerAdapterFrom.start();

                /*if (spinnerAdapterFrom.getFlag()==true){
                    Log.v("Place",spinnerAdapterFrom.getItem(position).getName());
                    fromResult= spinnerAdapterFrom.getItem(position);
                    Toast.makeText(getApplicationContext(),"Началом маршрута выбрано " + fromResult.getName(),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    spinnerAdapterFrom.start();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinnerAdapterTo.start();
                toResult=spinnerAdapterTo.getItem(position);
                if (spinnerAdapterTo.getFlag().equals(true))
                {
                    Toast.makeText(getApplicationContext(),"Концом маршрута выбрано " + toResult.getName(),Toast.LENGTH_SHORT).show();
                }
                spinnerAdapterTo.start();



                /*if (spinnerAdapterTo.getFlag()==true){
                    toResult=spinnerAdapterTo.getItem(position);
                    Toast.makeText(getApplicationContext(),"Концом маршрута выбрано " + toResult.getName(),Toast.LENGTH_SHORT).show();
                }
                else {
                    spinnerAdapterTo.start();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((fromResult!=null)&&(toResult!=null))
                {
                    Intent intent=new Intent(getApplicationContext(),MakeTravelNextActivity.class);
                    utils.setToTravel(toResult);
                    utils.setFromTravel(fromResult);
                    intent.putExtra(fromTravel,fromResult.getPlaceId());
                    Log.v("LOG",fromResult.getPlaceId());
                    intent.putExtra(toTravel,toResult.getPlaceId());
                    Log.v("LOG",toResult.getPlaceId());
                    startActivity(intent);
                }
                else
                {
                    Snackbar.make(v, "Выберите точки отправки и прибытия", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }


    public void addMyLocationToTravel(){
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
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
            try {
                if (loc.getProvider().equals(LocationManager.NETWORK_PROVIDER) || loc.getProvider().equals(locationManager.GPS_PROVIDER)) {
                    latitude = loc.getLatitude();
                    longitude = loc.getLongitude();
                }
            }catch (Exception e)
            {
                Toast.makeText(this,"Включите GPS",Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            Toast.makeText(this,"Включите GPS",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return super.onOptionsItemSelected(item);
    }



}
