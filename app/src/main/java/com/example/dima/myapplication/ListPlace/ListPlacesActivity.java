package com.example.dima.myapplication.ListPlace;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dima.myapplication.Direction.DirectionAPI;
import com.example.dima.myapplication.Direction.DirectionResults;
import com.example.dima.myapplication.Direction.Route;
import com.example.dima.myapplication.Direction.RouteDecode;
import com.example.dima.myapplication.Direction.Steps;
import com.example.dima.myapplication.MakeTravelActivity;
import com.example.dima.myapplication.MapsActivity;
import com.example.dima.myapplication.Place.PlaceAPI;
import com.example.dima.myapplication.Place.Places;
import com.example.dima.myapplication.Place.Result;
import com.example.dima.myapplication.R;
import com.example.dima.myapplication.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListPlacesActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    ProgressDialog progressDialog;
    Utils utils;

    LocationManager locationManager;
    Double longitude;
    Double latitude;
    Location loc;
    ViewPager viewPager;
    TabLayout tabLayout;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_list_places);
       utils=Utils.getInstance();
       List<Result> nullList=new ArrayList<Result>();
       utils.setNearbyPlaces(nullList);
       utils.setSelectPlaces(nullList);
       utils.setMyTravel(nullList);




       progressDialog = new ProgressDialog(this);
       progressDialog.setTitle("Please wait!");
       progressDialog.setMessage("Loading...");
       progressDialog.show();

       String type = "art_gallery|city_hall|museum|park|zoo";

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
               build_retrofit_and_get_response(type);
           }catch (Exception e)
           {
               progressDialog.dismiss();
               Toast.makeText(this,"Включите GPS",Toast.LENGTH_LONG).show();
           }

       }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(this,"Включите GPS",Toast.LENGTH_LONG).show();
        }


       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       final ActionBar ab = getSupportActionBar();
       //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
       ab.setDisplayHomeAsUpEnabled(true);

       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       if (navigationView != null) {
           setupDrawerContent(navigationView);
       }

       viewPager = (ViewPager) findViewById(R.id.viewpager);
       tabLayout = (TabLayout) findViewById(R.id.tabs);


       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
           }
       });

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getApplicationContext(), MakeTravelActivity.class);
               startActivity(intent);
           }
       });



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

                if (viewPager != null) {
                    setupViewPager(viewPager);
                }
                tabLayout.setupWithViewPager(viewPager);
                progressDialog.dismiss();
                //build_retrofit_and_get_duration_in_response();
            }

            @Override
            public void onFailure(Call<Places> call, Throwable t) {

            }

        });
    }




   /* private void build_retrofit_and_get_duration_in_response() {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DirectionAPI service = retrofit.create(DirectionAPI.class);

        int size = utils.getNearbyPlaces().size();
        Double lat1 = utils.getNearbyPlaces().get(0).getGeometry().getLocation().getLat();
        Double lon1 = utils.getNearbyPlaces().get(0).getGeometry().getLocation().getLng();

        Double lat2 = utils.getNearbyPlaces().get(1).getGeometry().getLocation().getLat();
        Double lon2 = utils.getNearbyPlaces().get(1).getGeometry().getLocation().getLng();
        String waypoint = "";
        if (utils.getNearbyPlaces().size() > 2) {
            waypoint = "optimize:true|";
            int max = 0;
            if (size > 10) {
                size = 3;
            }
            for (int i = 2; i < size; i++) {
                if (i == size - 1) {
                    waypoint += utils.getNearbyPlaces().get(i).getGeometry().getLocation().getLat().toString() +
                            "," + utils.getNearbyPlaces().get(i).getGeometry().getLocation().getLng().toString();
                } else {
                    waypoint += utils.getNearbyPlaces().get(i).getGeometry().getLocation().getLat().toString() +
                            "," + utils.getNearbyPlaces().get(i).getGeometry().getLocation().getLng().toString() + "|";
                }


            }
        }

        Call<DirectionResults> call = service.getJson(lat1.toString() + "," + lon1.toString(), lat2.toString() + "," + lon2.toString(), waypoint);

        call.enqueue(new Callback<DirectionResults>() {

            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
                Log.v("URLLLL",call.request().toString());
                DirectionResults directionResults = response.body();
                progressDialog.dismiss();
                ArrayList<LatLng> routelist = new ArrayList<LatLng>();
                if (directionResults.getRoutes().size() > 0) {
                    ArrayList<LatLng> decodelist;
                    Route routeA = directionResults.getRoutes().get(0);
                    if (routeA.getLegs().size() > 0) {
                        for (int k = 0; k < routeA.getLegs().size(); k++) {
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
                if (viewPager != null) {
                    setupViewPager(viewPager);
                }
                tabLayout.setupWithViewPager(viewPager);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {

            }
        });
    }*/


    // это менюшка
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }
*/

    /*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                menu.findItem(R.id.menu_night_mode_system).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                menu.findItem(R.id.menu_night_mode_auto).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                menu.findItem(R.id.menu_night_mode_night).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                menu.findItem(R.id.menu_night_mode_day).setChecked(true);
                break;
        }
        return true;
    }
*/

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_night_mode_system:
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case R.id.menu_night_mode_day:
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.menu_night_mode_night:
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.menu_night_mode_auto:
                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new PlaceListFragment(), "Nearby Places");
        adapter.addFragment(new PlaceListFragment2(), "My Places");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


}
