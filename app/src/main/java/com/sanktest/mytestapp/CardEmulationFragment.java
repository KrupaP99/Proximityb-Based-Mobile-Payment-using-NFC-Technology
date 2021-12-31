/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sanktest.mytestapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sanktest.mytestapp.logger.Log;

import static com.sanktest.mytestapp.LoginActivity.ipurl;

/**
 * Generic UI for sample discovery.
 */
public class CardEmulationFragment extends Fragment  implements LocationListener{

    public static final String TAG = "CardEmulationFragment";

    String url = "http://"+ipurl+"/PaymentGatewayApi/api/issuerbank.php?";

    String panNo, panExpiryMth, panExpiryYr, panCVVNo, userid;

    String spaymethod,payto;

    RequestQueue rq;

    EditText et;
    AlertDialog alertDialog;
    String tpinStr;
    EditText account;
    String imei,carriername,model,oslang;
    TelephonyManager telephonyManager;
    String userlocLatitude ="0", userlocLongitude = "0";

    RadioButton bankbtn, walletbtn;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String account1;
    /** Called when sample is created. Displays generic UI with welcome text. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_fragment2, container, false);
        account = (EditText) v.findViewById(R.id.card_account_field);

        bankbtn = (RadioButton) v.findViewById(R.id.bankRadio);
        walletbtn = (RadioButton) v.findViewById(R.id.walletRadio);

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        imei = telephonyManager.getDeviceId();
        carriername = telephonyManager.getNetworkOperatorName();
        model = Build.MODEL;

        //editor = sharedpreferences.edit();
        //sharedpreferences.getString("PrimaryBankAcc", "");


        Button makePayment = (Button) v.findViewById(R.id.makePay);

//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("Location Permission","Not given");
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
//        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
//        userlocLatitude = String.valueOf(location.getLatitude());
//        userlocLatitude = String.valueOf(location.getLongitude());

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                if (bankbtn.isChecked()){
                    alertBox();
                } else if (walletbtn.isChecked()){
                    alertBox();
                } else {
                    Toast.makeText(getActivity(),"Select Any of the above payment option", Toast.LENGTH_SHORT).show();
                }*/

                alertBox();

            }
        });

        //account.setText(AccountStorage.GetAccount(getActivity()));
        account.addTextChangedListener(new AccountUpdater());

        rq = Volley.newRequestQueue(getActivity());

        return v;
    }

    @Override
    public void onLocationChanged(Location location) {
        userlocLatitude = String.valueOf(location.getLatitude());
        userlocLongitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }


    private class AccountUpdater implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not implemented.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not implemented.
        }

        @Override
        public void afterTextChanged(Editable s) {
            account1 = s.toString();
            AccountStorage.SetAccount(getActivity(), account1);

        }
    }


    public void requestToken(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                //account1 = response;

                //confirm nahi hai ye error wala hai shayad se
                AccountStorage.SetAccount(getActivity(), response);
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                alertBoxTap();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(stringRequest);

    }

    public void alertBox() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(getActivity());
        tv.setText("ENTER TRANSACTION PIN");
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        et = new EditText(getActivity());
        //otpStr = et.getText().toString();

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        alertDialogBuilder.setView(layout);
        //alertDialogBuilder.setTitle("Enter OTP");
        // alertDialogBuilder.setMessage("Input Student ID");
        alertDialogBuilder.setCustomTitle(tv);

        //if (isError)
        //alertDialogBuilder.setIcon(R.drawable.icon_warning);
        // alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);

        // Setting Negative "Cancel" Button
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        // Setting Positive "OK" Button
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                    /*if (isError)
                        finish();
                    else {
                        Intent intent = new Intent(ChangeDeviceActivity.this,
                                MyPageActivity.class); startActivity(intent);
                    }*/

                sharedpreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();

                if(bankbtn.isChecked()){

                    panNo = sharedpreferences.getString("Pan", "");
                    panExpiryMth = sharedpreferences.getString("ExpMonth", "");
                    panExpiryYr = sharedpreferences.getString("ExpYear", "");
                    panCVVNo = sharedpreferences.getString("CVV", "");
                    userid = sharedpreferences.getString("UserId", "");
                    spaymethod = "bank";
                }
                if(walletbtn.isChecked()){

                    panNo = "1478258936982587";
                    panExpiryMth = "03";
                    panExpiryYr = "25";
                    panCVVNo = "258";
                    userid = sharedpreferences.getString("UserId", "");
                    spaymethod = "wallet";
                }

                bankbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        panNo = sharedpreferences.getString("Pan", "");
                        panExpiryMth = sharedpreferences.getString("ExpMonth", "");
                        panExpiryYr = sharedpreferences.getString("ExpYear", "");
                        panCVVNo = sharedpreferences.getString("CVV", "");
                        userid = sharedpreferences.getString("UserId", "");
                        spaymethod = "bank";

                    }
                });

                walletbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        panNo = "1478258936982587";
                        panExpiryMth = "03";
                        panExpiryYr = "25";
                        panCVVNo = "258";
                        userid = sharedpreferences.getString("UserId", "");
                        spaymethod = "wallet";

                    }
                });


                tpinStr = et.getText().toString();
                long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

                url = url + "tokenRequestId=" + number +"&pan="+ panNo +"&panExpMonth="+ panExpiryMth +"&panExpYear="+ panExpiryYr +"&carriername=" + carriername + "&oslang=" + oslang + "&imei=" + imei + "&model="+ model + "&userId="+ userid +"&amt=";

                url = url + account.getText().toString() +"&mpass=" + tpinStr + "&spaymentMethod=" + spaymethod + "&latitude=" + userlocLatitude + "&longitude=" + userlocLongitude;
                requestToken();
                //Toast.makeText( getActivity(), tpinStr, Toast.LENGTH_LONG).show();

            }
        });

        alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would
            // not display the 'Force Close' message
            e.printStackTrace();
        }

    }



    public void alertBoxTap() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(getActivity());
        tv.setText("TAP IN NOW");
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(40);

        //et = new EditText(getActivity());
        //otpStr = et.getText().toString();

        //LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //tv1Params.bottomMargin = 5;
        //layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        alertDialogBuilder.setView(layout);
        //alertDialogBuilder.setTitle("Enter OTP");
        // alertDialogBuilder.setMessage("Input Student ID");
        alertDialogBuilder.setCustomTitle(tv);

        //if (isError)
        //alertDialogBuilder.setIcon(R.drawable.icon_warning);
        // alertDialogBuilder.setMessage(message);
        //alertDialogBuilder.setCancelable(false);



        // Setting Positive "OK" Button
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                    /*if (isError)
                        finish();
                    else {
                        Intent intent = new Intent(ChangeDeviceActivity.this,
                                MyPageActivity.class); startActivity(intent);
                    }*/
                dialog.dismiss();
                //Toast.makeText(AddAccountActivity.this, otpStr, Toast.LENGTH_LONG).show();

            }
        });

        alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would
            // not display the 'Force Close' message
            e.printStackTrace();
        }

    }


}
