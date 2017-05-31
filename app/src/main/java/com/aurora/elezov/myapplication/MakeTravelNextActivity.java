package com.aurora.elezov.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aurora.elezov.myapplication.Direction.DirectionAPI;
import com.aurora.elezov.myapplication.Direction.DirectionResults;
import com.aurora.elezov.myapplication.Direction.Route;
import com.aurora.elezov.myapplication.Direction.RouteDecode;
import com.aurora.elezov.myapplication.Direction.Steps;
import com.aurora.elezov.myapplication.Place.Result;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MakeTravelNextActivity extends AppCompatActivity {
    Utils utils;
    RecyclerView rv;
    public static RecyclerViewTravelNextAdapter MyAdapter;
    TextView warningText;
    List<Result> data;
    private RecyclerView.LayoutManager mLayoutManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_travel_next);
        warningText=(TextView)findViewById(R.id.warning_text);
        utils=Utils.getInstance();
        Intent intent=getIntent();
        String str1=intent.getStringExtra(MakeTravelActivity.fromTravel);
        String str2=intent.getStringExtra(MakeTravelActivity.toTravel);
        data=utils.getSelectPlaces();

        Iterator<Result> i=data.iterator();
        while (i.hasNext())
        {
            Result s=i.next();
            if (s.getPlaceId().equals(str1)||s.getPlaceId().equals(str2)){
                i.remove();
            }
        }

        if (data.size()>8) {
            warningText.setText("Необходимо оставить лишь 8 мест");
        }
        else
        {
            warningText.setText("Маршрут может быть построен");
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        rv = (RecyclerView)findViewById(R.id.recyclerview_travel_next);
        mLayoutManager=new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        Log.v("LOG",""+data.size());
        MyAdapter = new RecyclerViewTravelNextAdapter();
        MyAdapter.add(data);
        Log.v("LOG",""+MyAdapter.getItemCount());
        //MyAdapter.notifyDataSetChanged();
        rv.setAdapter(MyAdapter);
        Log.v("LOG",""+rv.getAdapter().getItemCount());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.size()>8)
                    Snackbar.make(view, "Маршрут не может быть построен", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                else
                {
                    progressDialog=new ProgressDialog(getApplicationContext());
                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Loading...");
                    List<Result> travel=new ArrayList<Result>();
                    travel.add(utils.fromTravel);
                    travel.add(utils.toTravel);
                    for (int i=0;i<data.size();i++)
                        travel.add(data.get(i));
                    utils.setMyTravel(travel);
                    Log.v("COUNT MY TRAVEL",""+travel.size());
                    build_retrofit_and_get_duration_in_response(travel);
                }
            }
        });

    }

    private void build_retrofit_and_get_duration_in_response(List<Result> data)
    {
        String url = "https://maps.googleapis.com/maps/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DirectionAPI service = retrofit.create(DirectionAPI.class);
        Log.v("COUNT DATA",""+data.size());
        int size = data.size();
        Double lat1 = data.get(0).getGeometry().getLocation().getLat();
        Double lon1 = data.get(0).getGeometry().getLocation().getLng();

        Double lat2 = data.get(1).getGeometry().getLocation().getLat();
        Double lon2 = data.get(1).getGeometry().getLocation().getLng();
        String waypoint = "";
        if (size > 2) {
            waypoint = "optimize:true|";
            for (int i = 2; i < size; i++) {
                if (i == size - 1) {
                    waypoint += data.get(i).getGeometry().getLocation().getLat().toString() +
                            "," + data.get(i).getGeometry().getLocation().getLng().toString();
                } else {
                    waypoint += data.get(i).getGeometry().getLocation().getLat().toString() +
                            "," + data.get(i).getGeometry().getLocation().getLng().toString() + "|";
                }
            }
        }
        Log.v("WAYPOINT",waypoint);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String ListPreference = prefs.getString("listPref", "-1");
        String modeValue="driving";
        switch (ListPreference){
            case "-1":
                modeValue="driving";
                break;
            case "0":
                modeValue="walking";
                break;
        }
        Call<DirectionResults> call = service.getJson(lat1.toString() + "," + lon1.toString(), lat2.toString() + "," + lon2.toString(), waypoint,modeValue);
        Log.v("URL",call.request().url().toString());
        call.enqueue(new Callback<DirectionResults>() {

            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
                Log.v("HAHA","SUKAA");
                Log.v("URL",""+call.request().url().toString());
                DirectionResults directionResults = response.body();
                ArrayList<LatLng> routelist = new ArrayList<LatLng>();
                if (directionResults.getRoutes().size() > 0) {
                    ArrayList<LatLng> decodelist;
                    Route routeA = directionResults.getRoutes().get(0);
                    Log.v("Count Routes",""+directionResults.getRoutes().size());
                    if (routeA.getLegs().size() > 0) {
                        for (int k = 0; k < routeA.getLegs().size(); k++) {
                            List<Steps> steps = routeA.getLegs().get(k).getSteps();
                            Steps step;
                            com.aurora.elezov.myapplication.Direction.Location location;
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
                Log.v("COUNT DIRECTION MAKE",""+routelist.size());
                utils.setDirResults(routelist);

                progressDialog.dismiss();
                Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
