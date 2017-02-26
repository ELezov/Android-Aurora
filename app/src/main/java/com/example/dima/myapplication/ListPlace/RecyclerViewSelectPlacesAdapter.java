package com.example.dima.myapplication.ListPlace;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dima.myapplication.Place.Result;
import com.example.dima.myapplication.R;
import com.example.dima.myapplication.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 25.02.2017.
 */

public class RecyclerViewSelectPlacesAdapter extends RecyclerView.Adapter<RecyclerViewSelectPlacesAdapter.ViewHolder> {

    Utils utils;

    List<Result> data=new ArrayList<Result>();

    public RecyclerViewSelectPlacesAdapter() {
        utils= Utils.getInstance();
    }

    public void add(List<Result> items)
    {
        this.data=items;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_select_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.placeNameText.setText(data.get(position).getName());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //utils.addToSelectPlaces(data.get(position));
                Log.v("MyNearbyPlaces",""+utils.getSelectPlaces().size());

            }
        });

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PlaceDetailActivity.class);
                intent.putExtra(PlaceDetailActivity.PlACE_NAME, data.get(position).getName());
                intent.putExtra(PlaceDetailActivity.ID_PLACE,data.get(position).getPlaceId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View v;
        TextView placeNameText;
        Button deleteBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            v=itemView;
            placeNameText=(TextView)v.findViewById(R.id.select_place_name);
            deleteBtn=(Button)v.findViewById(R.id.deleteButton);
        }
    }
}
