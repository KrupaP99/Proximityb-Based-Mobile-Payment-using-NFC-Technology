package com.sanktest.mytestapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Locale;

import static com.sanktest.mytestapp.LoginActivity.ipurl;

public class RegisterActivity extends AppCompatActivity {

    String imei,carriername,model,os,lang,manufacturer,oslang,email;
    TelephonyManager telephonyManager;
    Locale locale;

    TextView TVimei,TVmodel,TVos,TVlang,TVmanufacturer;
    EditText ETfn,ETln,ETun,ETpass,ETmn,ETtpin,ETEmail;

    String firstname,lastname,username,password,mobileno,tpin;

    RequestQueue requestQueue;
    String url;

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    String urlEncoded;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        /*TVimei = (TextView) findViewById(R.id.imei);
        TVmodel = (TextView) findViewById(R.id.model);
        TVos = (TextView) findViewById(R.id.os);
        TVlang = (TextView) findViewById(R.id.lang);
        TVmanufacturer = (TextView) findViewById(R.id.manufacturer);*/

        ETfn = (EditText) findViewById(R.id.fnval);
        ETln = (EditText) findViewById(R.id.lnval);
        ETun = (EditText) findViewById(R.id.unval);
        ETpass = (EditText) findViewById(R.id.passval);
        ETmn = (EditText) findViewById(R.id.mnval);
        ETtpin = (EditText) findViewById(R.id.tpinval);
        ETEmail = (EditText) findViewById(R.id.et_email);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        imei = telephonyManager.getDeviceId();
        carriername = telephonyManager.getNetworkOperatorName();
        model = Build.MODEL;
        os = String.valueOf(Build.VERSION.RELEASE);
        oslang = Locale.getDefault().getDisplayLanguage();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            lang = String.valueOf(locale);
        } else {
            //noinspection deprecation
            locale = Resources.getSystem().getConfiguration().locale;
            lang = String.valueOf(locale);
        }

        manufacturer = Build.MANUFACTURER;

        /*TVimei.setText("IMEI: "+imei);
        TVmodel.setText("Model: "+model);
        TVos.setText("Android Version: "+os);
        TVlang.setText("Language: "+lang);
        TVmanufacturer.setText("Manufacturer: "+manufacturer);*/

        requestQueue = Volley.newRequestQueue(this);

    }

    public void logAccount(View view){

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

    }

    public void submit(View view) {

        firstname = ETfn.getText().toString();
        lastname = ETln.getText().toString();
        username = ETun.getText().toString();
        password = ETpass.getText().toString();
        mobileno = ETmn.getText().toString();
        tpin = ETtpin.getText().toString();
        email = ETEmail.getText().toString();

        if(!firstname.equals("")&&!lastname.equals("")&&!username.equals("")&&!password.equals("")&&!mobileno.equals("")&&!email.equals("")){

            //old url = "http://192.168.0.100/PaymentGatewayApi/getregister.php?"+"fn="+firstname+"&ln="+lastname+"&un="+username+"&pass="+password+"&mn="+mobileno+"&imei="+imei+"&model="+model+"&androidver="+os+"&lang="+lang+"&manufacturer="+manufacturer;
            url = "http://"+ipurl+"/PaymentGatewayApi/getregister.php?"+"fn="+firstname+"&ln="+lastname+"&email="+email+"&un="+username+"&pass="+password+"&mn="+mobileno+"&carriername="+carriername+"&oslang="+oslang+"&imei="+imei+"&manufacturer="+manufacturer+"&model="+model+"&tpin="+tpin;

            urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEncoded, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    if(response.equals("success")){

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                    } else {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(RegisterActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();

                }
            });

            requestQueue.add(stringRequest);

            //Toast.makeText(RegisterActivity.this, "Registered Sucessfully "+url, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(RegisterActivity.this, "Enter All Values", Toast.LENGTH_LONG).show();
        }

    }
}
