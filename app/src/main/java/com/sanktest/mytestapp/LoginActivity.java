package com.sanktest.mytestapp;
import android.provider.Settings.Secure;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

public class LoginActivity extends AppCompatActivity {

    public static final String ipurl = "bhavin.buzz";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    EditText userNameLogin, passLogin;
    Button checkLogin;

    String urLogin, urPassword, urImei, loginUrl, loginUrlEncoded;
    String errorValue, userID;

    TelephonyManager telephonyManager;

    RequestQueue requestQueue;


    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().hide();

        userNameLogin = (EditText) findViewById(R.id.usernval);
        passLogin = (EditText) findViewById(R.id.passlogval);

        requestQueue = Volley.newRequestQueue(this);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        urImei = telephonyManager.getDeviceId();

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        /*
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Toast.makeText(LoginActivity.this, "Location. "+location.getLatitude()+", "+location.getLongitude(), Toast.LENGTH_LONG).show();
        */

    }

    public void regAccount(View view){

        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

    }

    public void submitLogin(View view){

        urLogin = userNameLogin.getText().toString();
        urPassword = passLogin.getText().toString();

        if(!urLogin.equals("")&&!urPassword.equals("")){

            loginUrl = "http://"+ipurl+"/PaymentGatewayApi/getlogin.php?urlg="+urLogin+"&passlg="+urPassword+"&imeilg="+urImei;

            StringRequest loginStringRequest = new StringRequest(Request.Method.GET, loginUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {



                    /*if(response.equals("success")){

                        editor = sharedpreferences.edit();
                        editor.putString("Username", urLogin);
                        editor.commit();

                        startActivity(new Intent(LoginActivity.this, AddAccountActivity.class));

                    } else {

                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();

                    }*/

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        errorValue = jsonObject.getString("error");

                        if (errorValue.equals("false")){

                            userID = jsonObject.getString("userid");

                            editor = sharedpreferences.edit();
                            editor.putString("Username", urLogin);
                            editor.putString("UserId", userID);
                            editor.commit();

                            startActivity(new Intent(LoginActivity.this, AddAccountActivity.class));

                        } else {

                            editor = sharedpreferences.edit();
                            editor.putString("Username", "");
                            editor.putString("UserId", "");
                            editor.commit();

                            String errormsg = jsonObject.getString("msg");

                            Toast.makeText(LoginActivity.this, errormsg, Toast.LENGTH_LONG).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(LoginActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();

                }
            });

            requestQueue.add(loginStringRequest);

        } else {
            Toast.makeText(LoginActivity.this, "Username or Password is entered wrong.", Toast.LENGTH_LONG).show();
        }

    }



}
