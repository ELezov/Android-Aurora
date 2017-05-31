package com.aurora.elezov.myapplication

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.SeekBarPreference
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.SeekBar
import android.widget.Toast

class SettingsActivity : PreferenceActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        //setContentView(R.layout.activity_settings)
        //val toolbar = findViewById(R.id.toolbar) as Toolbar

        //var customPref=findPreference("customPref") as Preference?
       // var seekBar = findPreference("seekBarPref") as SeekBarPreference
        //var seekBarPref=findViewById("seekBarPref") as Preference?

        var mySeekBar=findPreference("your_pref_key") as com.pavelsikun.seekbarpreference.SeekBarPreference
        mySeekBar.onPreferenceChangeListener= Preference.OnPreferenceChangeListener { preference, newValue ->
            Toast.makeText(applicationContext,
                    "1",
                    Toast.LENGTH_LONG).show()


            true
        }







        /*seekBarPref!!.onPreferenceClickListener = android.support.v7.preference.Preference.OnPreferenceClickListener {
            Toast.makeText(applicationContext,
                    "1",
                    Toast.LENGTH_LONG).show()
            true
        }*/


        /*customPref!!.onPreferenceClickListener = OnPreferenceClickListener {
            //Toast.makeText(baseContext,
              //      "The custom preference has been clicked",
                //    Toast.LENGTH_LONG).show()
            val customSharedPreference = getSharedPreferences(
                    "myCustomSharedPrefs", Activity.MODE_PRIVATE)
            val editor = customSharedPreference
                    .edit()
            editor.putString("myCustomPref",
                    "The preference has been clicked")
            editor.commit()
            true
        }
        */
   // }


    }

}
