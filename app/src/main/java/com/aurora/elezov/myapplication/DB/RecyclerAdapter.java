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

import com.aurora.elezov.myapplication.R;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject obj = new JSONObject();

        String json = dbList.get(position).getRoute();

        try {
            obj = new JSONObject(json);
            try{
                 JSONArray jsonArray = obj.getJSONArray("PlaceName");
                 PlaceList = jsonArray.toString();

            }catch (JSONException e){}

        } catch (Throwable t) {}
        PlaceList = PlaceList.substring(3,PlaceList.length()-3);
        Log.i("111111111111111", PlaceList);
        holder.route.setText(PlaceList);
       // holder.route.setText(dbList.get(position).getRoute());
        //holder.email.setText(dbList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView route,email;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            route = (TextView) itemLayoutView
                    .findViewById(R.id.rvroute);
            //email = (TextView)itemLayoutView.findViewById(R.id.rvemail);
            itemLayoutView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
