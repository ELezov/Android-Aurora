package com.aurora.elezov.myapplication.ListPlace;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aurora.elezov.myapplication.Place.Place;
import com.aurora.elezov.myapplication.R;
import com.aurora.elezov.myapplication.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by USER on 19.02.2017.
 */

public class RecyclerViewNearbyPlacesAdapter extends RecyclerView.Adapter<RecyclerViewNearbyPlacesAdapter.ViewHolder> {

    private List<Place> data=new ArrayList<Place>();
    Utils utils;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageButton addButton;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(android.R.id.text1);
            addButton=(ImageButton)view.findViewById(R.id.addButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public Place getResultAt(int position) {
        return data.get(position);
    }

    public RecyclerViewNearbyPlacesAdapter() {

        utils=Utils.getInstance();
    }

    public void add(List<Place> items){
        data=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_nearby_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(data.get(position).getName());

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.addToSelectPlaces(data.get(position));
                Log.v("Select count",""+utils.getSelectPlaces().size());
                MyPlacesFragment.MyAdapter.add(utils.getSelectPlaces());
                MyPlacesFragment.MyAdapter.notifyDataSetChanged();

                List<Place> nerbP=utils.getNearbyPlaces();
                nerbP.remove(position);
                utils.setNearbyPlaces(nerbP);
                Log.v("Nearby count",""+utils.getNearbyPlaces().size());
                NearbyPlacesFragment.MyAdapter.add(utils.getNearbyPlaces());
                NearbyPlacesFragment.MyAdapter.notifyDataSetChanged();


            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
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
        return data.size();
    }
}
