package com.aurora.elezov.myapplication.ListPlace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aurora.elezov.myapplication.MakeTravelActivity;
import com.aurora.elezov.myapplication.MapsActivity;
import com.aurora.elezov.myapplication.Place.PlaceAPI;
import com.aurora.elezov.myapplication.Place.Places;
import com.aurora.elezov.myapplication.R;
import com.aurora.elezov.myapplication.Utils;

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
       ab.setDisplayHomeAsUpEnabled(true);

       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       if (navigationView != null) {
           setupDrawerContent(navigationView);
       }

       viewPager = (ViewPager) findViewById(R.id.viewpager);
       tabLayout = (TabLayout) findViewById(R.id.tabs);
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





    // это менюшка

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listplace, menu);
        //delete= menu!!.findItem(R.id.action_delete)
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId()==R.id.action_make_travel) {
            if (utils.getSelectPlaces().size()>1)
            {
                Intent intent=new Intent(getApplicationContext(), MakeTravelActivity.class);
                startActivity(intent);
            }
            else Toast.makeText(getApplicationContext(),"Вы не выбрали достопримечательности",Toast.LENGTH_LONG).show();

        }else onBackPressed();


        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new NearbyPlacesFragment(), "Nearby Places");
        adapter.addFragment(new MyPlacesFragment(), "My Places");
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
