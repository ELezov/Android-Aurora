package com.aurora.elezov.myapplication.ListPlace

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

import com.aurora.elezov.myapplication.Place.Place
import com.aurora.elezov.myapplication.R
import com.aurora.elezov.myapplication.Utils

import java.util.ArrayList

/**
 * Created by USER on 25.02.2017.
 */

class RecyclerViewSelectPlacesAdapter : RecyclerView.Adapter<RecyclerViewSelectPlacesAdapter.ViewHolder>() {

    internal var utils: Utils


    private var data: List<Place> = ArrayList()

    init {

        utils = Utils.getInstance()
    }

    fun add(items: List<Place>) {
        data = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_select_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.placeNameText.text = data[position].name

        holder.deleteBtn.setOnClickListener {
            utils.addToNearbyPlaces(data[position])
            Log.v("Nearby count", "" + utils.getNearbyPlaces().size)
            NearbyPlacesFragment.MyAdapter!!.add(utils.getNearbyPlaces())
            NearbyPlacesFragment.MyAdapter!!.notifyDataSetChanged()

            val selectP = utils.getSelectPlaces()
            selectP.removeAt(position)
            utils.setSelectPlaces(selectP)
            Log.v("Select count", "" + utils.getSelectPlaces().size)
            MyPlacesFragment.MyAdapter.add(utils.getSelectPlaces())
            MyPlacesFragment.MyAdapter.notifyDataSetChanged()
        }

        holder.v.setOnClickListener { v ->
            val context = v.context
            val intent = Intent(context, PlaceDetailActivity::class.java)
            intent.putExtra(PlaceDetailActivity.PlACE_NAME, data[position].name)
            intent.putExtra(PlaceDetailActivity.ID_PLACE, data[position].placeId)
            context.startActivity(intent)
        }


    }


    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        internal var placeNameText: TextView
        internal var deleteBtn: ImageButton

        init {
            placeNameText = v.findViewById(R.id.select_place_name) as TextView
            deleteBtn = v.findViewById(R.id.deleteButton) as ImageButton
        }
    }
}
