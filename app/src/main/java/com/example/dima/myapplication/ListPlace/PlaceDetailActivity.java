package com.example.dima.myapplication.ListPlace;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.example.dima.myapplication.Details.DetailsPlaceAPI;
import com.example.dima.myapplication.Details.Result;
import com.example.dima.myapplication.Details.ResultDetail;
import com.example.dima.myapplication.Details.Review;
import com.example.dima.myapplication.R;
import com.example.dima.myapplication.Utils;

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
    TextView placeLtng;

    RecyclerView listReview;
    ReviewRecyclerAdapter myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String Name = intent.getStringExtra(PlACE_NAME);
        String placeID = intent.getStringExtra(ID_PLACE);

        myAdapter=new ReviewRecyclerAdapter();
        //myAdapter.notifyDataSetChanged();

        utils = Utils.getInstance();

        placeName = (TextView) findViewById(R.id.place_name);
        placeAddr = (TextView) findViewById(R.id.place_addr);
        placeLtng = (TextView) findViewById(R.id.place_ltng);

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
            public void onResponse(Call<ResultDetail> call, Response<ResultDetail> response) {
                Log.v("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    Result place = response.body().getResult();
                    placeName.setText(place.getName());
                    placeAddr.setText(place.getAddressComponents().get(1).getShortName() + ","
                            + place.getAddressComponents().get(0).getShortName() + "\n" +
                            place.getAddressComponents().get(2).getShortName());
                    placeLtng.setText(place.getGeometry().getLocation().getLat().toString() + ","
                            + place.getGeometry().getLocation().getLng().toString());

                    Log.v("AAAA",""+place.getReviews().size());
                    myAdapter.add(place.getReviews());
                    //myAdapter.notifyDataSetChanged();
                    listReview.setAdapter(myAdapter);
                    Log.v("OHHHHHHH",""+listReview.getAdapter().getItemCount());
                    Log.v("Phone", response.body().getResult().getInternationalPhoneNumber().toString());
                    Log.v("Address", response.body().getResult().getAddressComponents().get(2).getLongName());
                    Log.v("Address", response.body().getResult().getFormattedAddress());
                    Log.v("Site", response.body().getResult().getWebsite());
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


    public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
        List<Review> data=new ArrayList<Review>();

        ReviewRecyclerAdapter() {

        }

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
