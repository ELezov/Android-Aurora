package com.aurora.elezov.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aurora.elezov.myapplication.ListPlace.ListPlacesActivity;
import com.aurora.elezov.myapplication.Place.Place;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Utils utils=Utils.getInstance();
    final String TAG = "myLogs";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Fragment fragment = null;
        Class fragmentClass = null;

/*

        //Get user info
        VKApi.users().get().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKApiUser user = ((VKList<VKApiUser>)response.parsedModel).get(0);
                Log.d("User name", user.first_name + " " + user.last_name);
            }
        });
        */

      //  shareWithDialog(getSupportFragmentManager());



        fragmentClass = FragmentMap.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed:" + connectionResult);
                    }
                    // methods go here
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Создадим новый фрагмент
        //Fragment fragment = null;
        //Class fragmentClass = null;




        if (id == R.id.nav_camera) {
            List<Place> nullList=new ArrayList<Place>();
            ArrayList<LatLng> nullDirections=new ArrayList<LatLng>();
            utils.setNearbyPlaces(nullList);
            utils.setSelectPlaces(nullList);
            utils.setMyTravel(nullList);
            utils.setDirResults(nullDirections);
            Intent intent = new Intent(this, ListPlacesActivity.class);
            startActivity(intent);
            //fragmentClass = FirstFragment.class;
        } else if (id == R.id.nav_gallery) {
            //fragmentClass = SecondFragment.class;
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });
            //fb
             LoginManager.getInstance().logOut();
            //vk
            VKSdk.logout();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

/*
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        // Вставляем фрагмент, заменяя текущий фрагмент
        // FragmentManager fragmentManager = getSupportFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    void shareWithDialog(FragmentManager fragmentManager) {

        VKShareDialogBuilder builder = new VKShareDialogBuilder();
        builder.setText("I created this post with VK Android SDK" +
                "\nSee additional information below\n#vksdk");
        builder.setAttachmentLink("VK Android SDK information",
                "https://vk.com/dev/android_sdk");
        builder.setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
            @Override
            public void onVkShareComplete(int postId) {
                // recycle bitmap if need
            }
            @Override
            public void onVkShareCancel() {
                // recycle bitmap if need
            }
            @Override
            public void onVkShareError(VKError error) {
                // recycle bitmap if need
            }
        });
        builder.show(fragmentManager, "VK_SHARE_DIALOG");
    }



}

