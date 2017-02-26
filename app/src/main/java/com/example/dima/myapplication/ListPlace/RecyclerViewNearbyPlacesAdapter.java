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
import java.util.List;


/**
 * Created by USER on 19.02.2017.
 */

public class RecyclerViewNearbyPlacesAdapter extends RecyclerView.Adapter<RecyclerViewNearbyPlacesAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<Result> data=new ArrayList<Result>();
    Utils utils;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        //public final ImageView mImageView;
        public Button addButton;
        public final TextView mTextView;
        public CheckBox mPlaceCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
            addButton=(Button)view.findViewById(R.id.addButton);
            //mPlaceCheckBox = (CheckBox) view.findViewById(R.id.checkBox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public Result getResultAt(int position) {
        return data.get(position);
    }

    public RecyclerViewNearbyPlacesAdapter(Context context) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;

        utils=Utils.getInstance();
        //mValues = items;
    }

    public void add(List<Result> items){
        data=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_nearby_place, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mBoundString = data.get(position).getName();
        holder.mTextView.setText(data.get(position).getName());

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.addToSelectPlaces(data.get(position));
                Log.v("MyNearbyPlaces",""+utils.getSelectPlaces().size());

            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PlaceDetailActivity.class);
                intent.putExtra(PlaceDetailActivity.PlACE_NAME, holder.mBoundString);
                intent.putExtra(PlaceDetailActivity.ID_PLACE,data.get(position).getPlaceId());

                context.startActivity(intent);
            }
        });

        /*holder.mPlaceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                utils.setFavorite(isChecked);
                if(isChecked) {
                    utils.setExample();
                    List<String> arr=new ArrayList<>();

                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        arr.add(i,response.body().getResults().get(i).getName());
                    }
                    System.out.println("да");


                } else {
                     PlaceListFragment2.
                    System.out.println("нет");
                }


            }
        });*/


        // надо разобраться
            /*
            Glide.with(holder.mImageView.getContext())
                    .load(Cheeses.getRandomCheeseDrawable())
                    .fitCenter()
                    .into(holder.mImageView);
                    */
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
