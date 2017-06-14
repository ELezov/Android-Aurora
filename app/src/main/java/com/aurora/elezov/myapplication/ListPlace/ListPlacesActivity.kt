package com.aurora.elezov.myapplication.ListPlace

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.aurora.elezov.myapplication.MakeTravelActivity
import com.aurora.elezov.myapplication.MapsActivity
import com.aurora.elezov.myapplication.Place.Place
import com.aurora.elezov.myapplication.Place.PlaceAPI
import com.aurora.elezov.myapplication.Place.PlacesResult
import com.aurora.elezov.myapplication.R
import com.aurora.elezov.myapplication.Utils

import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ListPlacesActivity : AppCompatActivity(),NearbyPlacesFragment.NextPageTokenListener {
    override fun onNextPageToken() {
       if (utils!!.currPageToken!=null) {
            build_retrofit_and_get_response(type, utils!!.currPageToken)
        }
    }

    var modeSort=""
    private var mDrawerLayout: DrawerLayout? = null
    internal var progressDialog: ProgressDialog?=null
    internal var utils: Utils?=null

    internal var locationManager: LocationManager?=null
    internal var longitude: Double? = null
    internal var latitude: Double? = null
    internal var loc: Location? = null
    internal var viewPager: ViewPager? = null
    internal var tabLayout: TabLayout?=null
    val type = "art_gallery|museum|park|zoo|aquarium"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_places)
        utils = Utils.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please wait!")
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.show()



        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
        } else {
            Log.e("DB", "PERMISSION GRANTED")
        }

        loc = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (loc == null)
            loc = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            try {
                if (loc!!.provider == LocationManager.NETWORK_PROVIDER || loc!!.provider == LocationManager.GPS_PROVIDER) {
                    latitude = loc!!.latitude
                    longitude = loc!!.longitude
                }
                build_retrofit_and_get_response(type,"")
            } catch (e: Exception) {
                progressDialog!!.dismiss()
                Toast.makeText(this, "Включите GPS", Toast.LENGTH_LONG).show()
            }

        } else {
            progressDialog!!.dismiss()
            Toast.makeText(this, "Включите GPS", Toast.LENGTH_LONG).show()
        }


        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)

        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }

        viewPager = findViewById(R.id.viewpager) as ViewPager
        tabLayout = findViewById(R.id.tabs) as TabLayout
    }


    private fun build_retrofit_and_get_response(type: String,nextToken:String) {

        utils!!.loading=true

        val prefs = PreferenceManager
                .getDefaultSharedPreferences(baseContext)
        var ListPreference = prefs.getString("listSortPref","-1")
        var radius="20000"
        when (ListPreference) {
            "-1" -> modeSort = "prominence"

            "0" -> {
                modeSort = "distance"
                radius=""
            }
        }

        Log.v("ModeSort",modeSort +"  "+ListPreference)
        val url = "https://maps.googleapis.com/maps/"

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(PlaceAPI::class.java)

        val call = service.getNearbyPlaces(type,nextToken, latitude.toString() + "," + longitude, modeSort,radius)

        call.enqueue(object : Callback<PlacesResult> {
            override fun onResponse(call: Call<PlacesResult>, response: Response<PlacesResult>) {
                Log.v("URL nearby PlacesResult", call.request().url().toString())
                if (nextToken!="")
                {
                    var curPlaces=utils!!.getNearbyPlaces()
                    var places=response.body().results
                    for (i in 0..places.size-1)
                    {
                        curPlaces.add(places.get(i))
                    }
                    NearbyPlacesFragment.MyAdapter!!.add(curPlaces)
                    MyPlacesFragment.MyAdapter.notifyDataSetChanged()
                    utils!!.setNearbyPlaces(curPlaces)
                }
                 else {
                    utils!!.setNearbyPlaces(response.body().results)
                }
                utils!!.setCurrPageToken(response.body().nextPageToken)
                if (viewPager != null) {
                    setupViewPager(viewPager!!)
                }
                tabLayout!!.setupWithViewPager(viewPager)
                progressDialog!!.dismiss()
                //build_retrofit_and_get_duration_in_response();
                utils!!.loading=false
            }

            override fun onFailure(call: Call<PlacesResult>, t: Throwable) {
                utils!!.loading=false
            }

        })
    }


    // это менюшка

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_listplace, menu)
        //delete= menu!!.findItem(R.id.action_delete)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_make_travel) {
            if (utils!!.getSelectPlaces().size > 1) {
                val intent = Intent(applicationContext, MakeTravelActivity::class.java)
                startActivity(intent)
            } else
                Toast.makeText(applicationContext, "Вы не выбрали достопримечательности", Toast.LENGTH_LONG).show()

        } else
            onBackPressed()


        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        val i = Intent(this, MapsActivity::class.java)
        startActivity(i)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(supportFragmentManager)
        adapter.addFragment(NearbyPlacesFragment(), "Nearby PlacesResult")
        adapter.addFragment(MyPlacesFragment(), "My PlacesResult")
        viewPager.adapter = adapter
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout!!.closeDrawers()
            true
        }
    }

    internal class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            mFragments.add(fragment)
            mFragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitles[position]
        }
    }


}
