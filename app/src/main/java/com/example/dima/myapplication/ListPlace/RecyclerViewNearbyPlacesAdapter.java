package com.example.dima.myapplication.ListPlace;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.myapplication.Place.Result;
import com.example.dima.myapplication.R;
import com.example.dima.myapplication.Utils;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;


/**
 * Created by USER on 19.02.2017.
 */

public class RecyclerViewNearbyPlacesAdapter extends RecyclerView.Adapter<RecyclerViewNearbyPlacesAdapter.ViewHolder> {

    private List<Result> data=new ArrayList<Result>();
    Utils utils;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Button addButton;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(android.R.id.text1);
            addButton=(Button)view.findViewById(R.id.addButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public Result getResultAt(int position) {
        return data.get(position);
    }

    public RecyclerViewNearbyPlacesAdapter() {

        utils=Utils.getInstance();
    }

    public void add(List<Result> items){
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
                PlaceListFragment2.MyAdapter.add(utils.getSelectPlaces());
                PlaceListFragment2.MyAdapter.notifyDataSetChanged();

                List<Result> nerbP=utils.getNearbyPlaces();
                nerbP.remove(position);
                utils.setNearbyPlaces(nerbP);
                Log.v("Nearby count",""+utils.getNearbyPlaces().size());
                PlaceListFragment.MyAdapter.add(utils.getNearbyPlaces());
                PlaceListFragment.MyAdapter.notifyDataSetChanged();


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
