package com.aurora.elezov.myapplication.DB;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aurora.elezov.myapplication.ListPlace.PlaceDetailActivity;
import com.aurora.elezov.myapplication.MakeTravelActivity;
import com.aurora.elezov.myapplication.MakeTravelNextActivity;
import com.aurora.elezov.myapplication.MapsActivity;
import com.aurora.elezov.myapplication.Place.Geometry;
import com.aurora.elezov.myapplication.Place.Place;
import com.aurora.elezov.myapplication.R;
import com.aurora.elezov.myapplication.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user_adnig on 11/14/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

  static   List<DatabaseModel> dbList;
    static  Context context;
    String PlaceList;
    Utils utils;
    RecyclerAdapter(Context context, List<DatabaseModel> dbList ){
        this.dbList = new ArrayList<DatabaseModel>();
        this.context = context;
        this.dbList = dbList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_row, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        JSONObject obj = new JSONObject();

        String json = dbList.get(position).getRoute();
        PlaceList=" ";
        try {
            obj = new JSONObject(json);
            try{
                 JSONArray jsonArray = obj.getJSONArray("places");

                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    PlaceList=PlaceList+jsonObject.get("name").toString()+" \n ";
                }



            }catch (JSONException e){}

        } catch (Throwable t) {}

        Log.i("111111111111111", PlaceList);
        holder.route.setText(PlaceList);
       // holder.route.setText(dbList.get(position).getRoute());
        //holder.email.setText(dbList.get(position).getEmail());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = dbList.get(position).getRoute();
                Log.v("PLACE", "ENTER IN");

                try {
                    JSONObject obj = new JSONObject(json);
                    try{
                        JSONArray array=obj.getJSONArray("places");
                        //JSONArray VicinityArray = obj.getJSONArray("Visinity");
                        Log.i("PLACE POLINA", array.get(0).toString());
                        List<Place> places=new ArrayList<Place>();
                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObject=array.getJSONObject(i);
                            Place place=new Place();
                            place.setPlaceId(jsonObject.get("place_id").toString());
                            place.setName(jsonObject.get("name").toString());
                            Double lat=Double.parseDouble(jsonObject.get("lat").toString());
                            Double lon=Double.parseDouble(jsonObject.get("lon").toString());
                            place.setVicinity(jsonObject.get("vicinity").toString());
                            Geometry geometry=new Geometry();
                            com.aurora.elezov.myapplication.Place.Location location=new com.aurora.elezov.myapplication.Place.Location();
                            location.setLng(lon);
                            location.setLat(lat);
                            geometry.setLocation(location);
                            place.setGeometry(geometry);
                            places.add(place);
                            Log.i("PLACE", places.get(i).getName()+" ; "+places.get(i).getGeometry().getLocation().getLat());
                            utils=Utils.getInstance();
                            utils.setSelectPlaces(places);
                            Context context = v.getContext();
                            Intent intent = new Intent(context, MakeTravelActivity.class);
                            context.startActivity(intent);
                        }
                    }catch (JSONException e){
                        Log.v("ERROR", e.getMessage().toString());
                    }

                } catch (Throwable t) {
                    Log.v("ERROR", t.getMessage().toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View v;

        public TextView route,email;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            v=itemLayoutView;
            route = (TextView) itemLayoutView
                    .findViewById(R.id.rvroute);
            //email = (TextView)itemLayoutView.findViewById(R.id.rvemail);

        }

    }
}
