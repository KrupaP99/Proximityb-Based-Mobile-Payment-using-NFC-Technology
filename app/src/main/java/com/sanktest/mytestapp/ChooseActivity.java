package com.sanktest.mytestapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sanktest.mytestapp.verify.VerifyMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sanktest.mytestapp.LoginActivity.ipurl;

public class ChooseActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String value, userid;

    String requestUrl = "http://" + ipurl + "/PaymentGatewayApi/fetchUser.php?panno=";


    RequestQueue accdetailrequest;

    TextView accName, accBank, accNo, accPanNo, accBal, walletBal, ExpDate, usermobile;

    String panNo, panExpiryMth, panExpiryYr, panCVVNo;
    String imei, model;
    TelephonyManager telephonyManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        Log.v("TAG", requestUrl);

        sharedPreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        value = sharedPreferences.getString("PrimaryBankAcc", "");
        userid = sharedPreferences.getString("UserId", "");


        panNo = sharedPreferences.getString("Pan", "");
        panExpiryMth = sharedPreferences.getString("ExpMonth", "");
        panExpiryYr = sharedPreferences.getString("ExpYear", "");
        panCVVNo = sharedPreferences.getString("CVV", "");
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        model = Build.MODEL;


        accdetailrequest = Volley.newRequestQueue(this);

        accName = (TextView) findViewById(R.id.userAccName);
        accBank = (TextView) findViewById(R.id.userBankName);
//        accNo = (TextView) findViewById(R.id.userAccNo);
        accPanNo = (TextView) findViewById(R.id.userPanNo);
        accBal = (TextView) findViewById(R.id.userBankBal);
        walletBal = (TextView) findViewById(R.id.userWalletBal);
        ExpDate = (TextView) findViewById(R.id.expDate);
        usermobile = (TextView) findViewById(R.id.userMobile);

        bankDetails();

    }

    public void sender(View view) {
        Intent intent = new Intent(ChooseActivity.this, VerifyMainActivity.class);
        startActivity(intent);
    }

    public void receiver(View view) {
        Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void viewAllTransaction(View view) {

        startActivity(new Intent(ChooseActivity.this, TransactionsActivity.class));

    }

    public void bankDetails() {

        requestUrl = requestUrl + value + "&userId=" + userid;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    JSONObject json;

                    try {
                        json = response.getJSONObject(i);

                        accName.setText(json.getString("accname"));
                        accBank.setText(json.getString("bankname"));
                        //accNo.setText("A/C No: "+json.getString("accno"));
                        accPanNo.setText("PAN No: " + json.getString("pan"));
                        accBal.setText("\u20B9" + json.getString("amt"));
                        walletBal.setText("\u20B9" + json.getString("wallet_money"));
                        ExpDate.setText("Expiry Date: " + json.getString("expmonth") + "/20" + json.getString("expyear"));
                        usermobile.setText(json.getString("mobileno"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error: ", String.valueOf(error));
            }
        });

        accdetailrequest.add(jsonArrayRequest);

    }

    public void logout(View view) {

        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        startActivity(new Intent(ChooseActivity.this, SplashActivity.class));


    }

    public void addMoney(View view) {

        Intent passIntent = new Intent(ChooseActivity.this, AddMoney.class);
        passIntent.putExtra("panNo", panNo);
        passIntent.putExtra("panExpiryMth", panExpiryMth);
        passIntent.putExtra("panExpiryYr", panExpiryYr);
        passIntent.putExtra("imei", imei);
        passIntent.putExtra("model", model);
        passIntent.putExtra("userid", userid);
        startActivity(passIntent);

    }

    public void transMoney(View view) {

        Intent passIntent = new Intent(ChooseActivity.this, TransferMoney.class);
        passIntent.putExtra("panNo", panNo);
        passIntent.putExtra("panExpiryMth", panExpiryMth);
        passIntent.putExtra("panExpiryYr", panExpiryYr);
        passIntent.putExtra("imei", imei);
        passIntent.putExtra("model", model);
        passIntent.putExtra("userid", userid);
        startActivity(passIntent);

    }
}
