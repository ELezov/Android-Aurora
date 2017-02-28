package com.example.dima.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dima.myapplication.Place.Result;

import java.util.List;

/**
 * Created by USER on 27.02.2017.
 */

public class SpinnerAdapter extends ArrayAdapter<Result> {
    Context context;
    List<Result> data;
    boolean flag = false;

    public SpinnerAdapter(Context context, int resource, List<Result> objects) {
        super(context, resource, objects);
        this.data=objects;
        this.context=context;
    }

    public void start(){
        this.flag=true;
    }

    public Boolean getFlag(){
        return flag;
    }

    public int getCount(){
        return data.size();
    }

    public Result getItem(int position)
    {
        return data.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label=(TextView) super.getView(position,convertView,parent);
        label.setTextSize(17);

        if (flag!=false)
        {
            label.setText(data.get(position).getName());
            label.setTextColor(Color.BLACK);
        }
        else
        {
            label.setText("Выберите точку");
            label.setTextColor(Color.BLACK);
        }

        //return super.getView(position, convertView, parent);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label=(TextView) super.getView(position,convertView,parent);
        label.setTextColor(Color.BLACK);
        label.setText(data.get(position).getName());
        label.setTextSize(13);
        return label;
    }
}
