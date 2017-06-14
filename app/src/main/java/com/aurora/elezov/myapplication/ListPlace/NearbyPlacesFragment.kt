package com.aurora.elezov.myapplication.ListPlace

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aurora.elezov.myapplication.Place.Place
import com.aurora.elezov.myapplication.R
import com.aurora.elezov.myapplication.Utils

import java.util.ArrayList


class NearbyPlacesFragment : Fragment()  {

    var  utils: Utils? =null
    var rv: RecyclerView?=null

    internal interface NextPageTokenListener {
        fun onNextPageToken()
    }

    private var clickCallBack: NextPageTokenListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rv = inflater!!.inflate(
                R.layout.fragment_nearby_places_list, container, false) as RecyclerView

        utils= Utils.getInstance()
        clickCallBack = activity as NextPageTokenListener

        var layoutManager=LinearLayoutManager(rv!!.context)


        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.getChildCount()//смотрим сколько элементов на экране
                val totalItemCount = layoutManager.getItemCount()//сколько всего элементов
                val firstVisibleItems = layoutManager.findFirstVisibleItemPosition()//какая позиция первого элемента


                if  (!utils!!.loading){//проверяем, грузим мы что-то или нет, эта переменная должна быть вне класса  OnScrollListener
                    if (visibleItemCount + firstVisibleItems >= totalItemCount) {
                        //ставим флаг что мы попросили еще элемены
                        if (clickCallBack != null) {
                            clickCallBack!!.onNextPageToken()//тут я использовал калбэк который просто говорит наружу что нужно еще элементов и с какой позиции начинать загрузку
                        }
                    }
                }
            }
        }
        rv!!.layoutManager = layoutManager
        rv!!.setOnScrollListener(scrollListener);
        utils = Utils.getInstance()



        val adapter = RecyclerViewNearbyPlacesAdapter()
        MyAdapter = RecyclerViewNearbyPlacesAdapter()
        var arr: List<Place> = ArrayList<Place>()
        arr = utils!!.getNearbyPlaces()
        MyAdapter!!.add(arr)
        adapter.notifyDataSetChanged()
        adapter.onButtonClickListener={->
            clickCallBack!!.onNextPageToken()
        }
        rv!!.adapter = MyAdapter
        return rv
    }

    companion object {
        var MyAdapter: RecyclerViewNearbyPlacesAdapter?=null
    }


}
