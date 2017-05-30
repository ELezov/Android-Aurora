package com.aurora.elezov.myapplication.ListPlace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.aurora.elezov.myapplication.Details.DetailsPlaceAPI;
import com.aurora.elezov.myapplication.Details.Result;
import com.aurora.elezov.myapplication.Details.ResultDetail;
import com.aurora.elezov.myapplication.Details.Review;
import com.aurora.elezov.myapplication.R;
import com.aurora.elezov.myapplication.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlaceDetailActivity extends AppCompatActivity {
    public static final String PlACE_NAME = "place_name";
    public static final String ID_PLACE = "id_place";

    Utils utils;

    TextView placeName;
    TextView placeAddr;
    //TextView placeLtng;
    TextView phoneText;
    TextView siteText;
    TextView openText;
    LinearLayout linearLayoutOpenHours;

    MapView mapView;


    RecyclerView listReview;
    ReviewRecyclerAdapter myAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String Name = intent.getStringExtra(PlACE_NAME);
        String placeID = intent.getStringExtra(ID_PLACE);

        mapView = (MapView) findViewById(R.id.map_detail);
        mapView.onCreate(savedInstanceState);



        myAdapter=new ReviewRecyclerAdapter();
        //myAdapter.notifyDataSetChanged();

        utils = Utils.getInstance();

        placeName = (TextView) findViewById(R.id.place_name);
        placeAddr = (TextView) findViewById(R.id.place_addr);
        //placeLtng = (TextView) findViewById(R.id.place_ltng);
        phoneText = (TextView) findViewById(R.id.phone_number);
        siteText=(TextView)findViewById(R.id.site);
        openText=(TextView) findViewById(R.id.opening_hours);
        linearLayoutOpenHours=(LinearLayout) findViewById(R.id.linearLayout_openHours);


        listReview = (RecyclerView) findViewById(R.id.list_review);
        listReview.setLayoutManager(new LinearLayoutManager(listReview.getContext()));


//        ImageView backdrop=(ImageView) findViewById(R.id.backdrop);
//        backdrop.setImageResource(R.mipmap.logo);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DetailsPlaceAPI service = retrofit.create(DetailsPlaceAPI.class);

        Call<ResultDetail> call = service.getPlaceDetails(placeID);

        call.enqueue(new Callback<ResultDetail>() {

            @Override
            public void onResponse(Call<ResultDetail> call, final Response<ResultDetail> response) {
                Log.v("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    final Result place = response.body().getResult();
                    placeName.setText(place.getName());
                    placeAddr.setText(place.getAddressComponents().get(1).getShortName() + ","
                            + place.getAddressComponents().get(0).getShortName() + "\n" +
                            place.getAddressComponents().get(2).getShortName());
                   // placeLtng.setText(place.getGeometry().getLocation().getLat().toString() + ","
                           // + place.getGeometry().getLocation().getLng().toString());

                    try {
                        Log.v("ReviewSize",""+place.getReviews().size());
                        myAdapter.add(place.getReviews());
                        listReview.setAdapter(myAdapter);
                        Log.v("ReviewCount",""+listReview.getAdapter().getItemCount());
                    }
                    catch (Exception e){
                        Log.v("Review",e.getMessage().toString());
                    }

                    try {
                        Log.v("Phone", response.body().getResult().getInternationalPhoneNumber().toString());
                        phoneText.setVisibility(View.VISIBLE);
                        phoneText.setText("Тел: "+response.body().getResult().getInternationalPhoneNumber().toString());
                    }catch (Exception e){
                        Log.v("Phone","fail");
                    }
                    try{
                        String openHours="";
                        int k=response.body().getResult().getOpeningHours().getWeekdayText().size();
                        List<String> list=response.body().getResult().getOpeningHours().getWeekdayText();
                        for (int i=0;i<k;i++)
                            openHours+=list.get(i)+"\n";
                        openText.setText(openHours);
                    }catch (Exception e){
                        Log.v("OpenHours",e.getMessage().toString());
                        linearLayoutOpenHours.setVisibility(View.INVISIBLE);
                    }


                    Log.v("Address", response.body().getResult().getAddressComponents().get(2).getLongName());
                    Log.v("Address", response.body().getResult().getFormattedAddress());
                    try{
                        Log.v("Site", response.body().getResult().getWebsite());
                        siteText.setVisibility(View.VISIBLE);
                        siteText.setText(response.body().getResult().getWebsite());
                        siteText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getResult().getWebsite()));
                                startActivity(browserIntent);
                            }
                        });
                    } catch (Exception e){
                        Log.v("Site","fail");
                    }



                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap map) {

                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            map.getUiSettings().setZoomControlsEnabled(true);
                            map.getUiSettings().setZoomGesturesEnabled(true);
                            map.setBuildingsEnabled(true);
                            map.getUiSettings().setScrollGesturesEnabled(true);

                            LatLng placeDet = new LatLng(place.getGeometry().getLocation().getLat(),
                                    place.getGeometry().getLocation().getLng());

                            map.addMarker(new MarkerOptions()
                                    .title(place.getName()
                                    )
                                    .position(
                                            placeDet)
                                    .snippet(place.getVicinity())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            );

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(placeDet, 10);
                            map.animateCamera(cameraUpdate);
                        }
                    });


                }
            }


            @Override
            public void onFailure(Call<ResultDetail> call, Throwable t) {

            }


        });


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(Name);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
        List<Review> data=new ArrayList<Review>();
        ReviewRecyclerAdapter() {}

        public void add(List<Review> items){
            this.data=items;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);

            //view.setBackgroundResource(mBackground);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.authorName.setText(data.get(position).getAuthorName());
            Log.v("HGAAGAG",data.get(position).getAuthorName());
            holder.reviewTime.setText(data.get(position).getRelativeTimeDescription());
            holder.reviewText.setText(data.get(position).getText());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View v;
            TextView authorName;
            TextView reviewTime;
            TextView reviewText;

            public ViewHolder(View itemView) {
                super(itemView);
                v=itemView;
                authorName=(TextView)v.findViewById(R.id.author_name);
                reviewTime=(TextView)v.findViewById(R.id.review_time);
                reviewText=(TextView)v.findViewById(R.id.review_text);
            }
        }
    }
}
