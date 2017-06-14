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
 * Created by USER on 19.02.2017.
 */

class RecyclerViewNearbyPlacesAdapter : RecyclerView.Adapter<RecyclerViewNearbyPlacesAdapter.ViewHolder>() {

    var data: List<Place> = ArrayList()
    internal var utils: Utils
    lateinit var onButtonClickListener:(()->Unit)



    fun getResultAt(position: Int): Place {
        return data[position]
    }

    init {

        utils = Utils.getInstance()
    }

    fun add(items: List<Place>) {
        data = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_nearby_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.text = data[position].name


        holder.addButton.setOnClickListener {
            utils.addToSelectPlaces(data[position])
            Log.v("Select count", "" + utils.getSelectPlaces().size)
            MyPlacesFragment.MyAdapter.add(utils.getSelectPlaces())
            MyPlacesFragment.MyAdapter.notifyDataSetChanged()

            val nerbP = utils.getNearbyPlaces()
            nerbP.removeAt(position)
            utils.setNearbyPlaces(nerbP)
            Log.v("Nearby count", "" + utils.getNearbyPlaces().size)
            NearbyPlacesFragment.MyAdapter!!.add(utils.getNearbyPlaces())
            NearbyPlacesFragment.MyAdapter!!.notifyDataSetChanged()
        }

        holder.mView.setOnClickListener { v ->
            val context = v.context
            val intent = Intent(context, PlaceDetailActivity::class.java)
            intent.putExtra(PlaceDetailActivity.PlACE_NAME, data[position].name)
            intent.putExtra(PlaceDetailActivity.ID_PLACE, data[position].placeId)

            context.startActivity(intent)
        }

       /* if (position==data.size-1)
        {
            onButtonClickListener.invoke()
        }*/


    }

    override fun getItemCount(): Int {
        return data.size;
    }

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var addButton: ImageButton
        val mTextView: TextView

        init {
            mTextView = mView.findViewById(android.R.id.text1) as TextView
            addButton = mView.findViewById(R.id.addButton) as ImageButton





        }

        override fun toString(): String {
            return super.toString() + " '" + mTextView.text
        }
    }

}
