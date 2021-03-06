package com.aurora.elezov.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aurora.elezov.myapplication.ListPlace.PlaceDetailActivity;
import com.aurora.elezov.myapplication.Place.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 28.02.2017.
 */

public class RecyclerViewTravelNextAdapter extends RecyclerView.Adapter<RecyclerViewTravelNextAdapter.ViewHolder>{
    Utils utils;
    private List<Place> data=new ArrayList<Place>();

    RecyclerViewTravelNextAdapter(){
        utils= Utils.getInstance();
    }

    public void add(List<Place> items){
        data=items;
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
                data.remove(position);
                MakeTravelNextActivity.MyAdapter.add(utils.getSelectPlaces());
                MakeTravelNextActivity.MyAdapter.notifyDataSetChanged();

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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View v;
        TextView placeNameText;
        ImageButton deleteBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            v=itemView;
            placeNameText=(TextView)v.findViewById(R.id.select_place_name);
            deleteBtn=(ImageButton)v.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
