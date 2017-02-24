package com.example.dima.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton button = (SignInButton) findViewById(R.id.signIn);

        // Создадим новый фрагмент


        button.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
               // Fragment fragment = null;
               // Class fragmentClass = null;
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
                //fragmentClass = FragmentMap.class;
               // try {
               //     fragment = (Fragment) fragmentClass.newInstance();
                //} catch (Exception e) {
                //    e.printStackTrace();
               // }
               // FragmentManager fragmentManager = getSupportFragmentManager();
               // fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });


    }
}
