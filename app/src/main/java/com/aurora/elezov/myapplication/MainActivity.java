package com.aurora.elezov.myapplication;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.aurora.elezov.myapplication.Details.Result;
import com.aurora.elezov.myapplication.Details.ResultDetail;
import com.aurora.elezov.myapplication.Social.UserInfoAPI;
import com.aurora.elezov.myapplication.Social.UserInfoDetail;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKApiUser;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private LoginButton loginButton;
    private Button fb_btn;
    private CallbackManager callbackManager;

    private boolean isResumed = false;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private String email_id;
    ProgressDialog progress;

    Utils utils;
    public String EMAIL = "ForTest";
    public static final int SOMEID = 4;
    public int ID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.fb_login);

        progress=new ProgressDialog(MainActivity.this);

        progress.setIndeterminate(false);
        progress.setCancelable(false);

        findViewById(R.id.google_login).setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
        }

        //Button signInButton = (SignInButton) findViewById(R.id.google_login);
        //  signInButton.setSize(SignInButton.SIZE_STANDARD);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (VKSdk.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
    }
/*
    @Override
    protected void onPause() {
        isResumed = false;
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }

*/

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                //Пользователь успешно авторизовался

                //  mStatusTextView.setText(getString(R.string.signed_in_fmt, VKSdk.getAccessToken().userId));

                build_retrofit();
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);


            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        }))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            EMAIL = acct.getEmail();
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void facebook_signIn() {

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        EMAIL = null;

                        try {

                            EMAIL = object.getString("email");
                            Log.v(TAG, object.toString());
                            Log.v(TAG, EMAIL);
                            String json = EMAIL.toString();
                            Log.d(TAG, "JSON TO STRING:" + json);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        Toast.makeText(getApplicationContext(), EMAIL, Toast.LENGTH_SHORT).show();

                        //build_retrofit();
                    }

                });
                Bundle parameters = new Bundle();

                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();
                build_retrofit();
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);




            }

            @Override
            public void onCancel() {
                // Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                //  Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }

        });

    }






    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);

            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    @Override
    public void onClick(View v) {
        if (!isOnline()) {
            Toast.makeText(getApplicationContext(),
                    "Нет соединения с интернетом!", Toast.LENGTH_LONG).show();
            return;
        } else {
            switch (v.getId()) {
                case R.id.google_login:
                    signIn();
                    build_retrofit();
                    break;
                case R.id.vk_login:
                    VKSdk.login(MainActivity.this, VKScope.WALL);
                    build_retrofit();
                    break;
                case R.id.fb_login:
                    if (AccessToken.getCurrentAccessToken() == null) {
                        facebook_signIn();

                    }

                    break;

            }
        }
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

    private void build_retrofit() {


        utils = Utils.getInstance();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(utils.AURORA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInfoAPI service = retrofit.create(UserInfoAPI.class);

        Call<UserInfoDetail> call = service.PostUserInfo(SOMEID, EMAIL);
        //Call<UserInfoDetail> call = service.PostUserInfo(EMAIL);
        call.enqueue(new Callback<UserInfoDetail>() {

            @Override
            public void onResponse(Call<UserInfoDetail> call, Response<UserInfoDetail> response) {
                Log.v("URL", call.request().url().toString());
                if (response.isSuccessful()) {


                }
            }

            @Override
            public void onFailure(Call<UserInfoDetail> call, Throwable t) {


            }
        });
    }
}


