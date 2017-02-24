package com.example.dima.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton button = (SignInButton) findViewById(R.id.signIn);

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if ( !isOnline() ){
                    Toast.makeText(getApplicationContext(),
                            "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }

    }
}
