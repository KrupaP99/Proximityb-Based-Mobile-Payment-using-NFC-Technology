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

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LOCATION_SERVICE;
import static com.sanktest.mytestapp.LoginActivity.ipurl;

/**
 * Generic UI for sample discovery.
 */
public class CardReaderFragment extends Fragment implements LoyaltyCardReader.AccountCallback {

    public static final String TAG = "CardReaderFragment";
    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public LoyaltyCardReader mLoyaltyCardReader;
    private TextView mAccountField; 

    String requestToken, transactionToken, senderid, senderaccno, amt, userid, userpanno, spaymentmethod, sscore;

    String res, transactionAmt, sAccNo, rAccNo, ttime, tsendername, trecievername, tsenderlatitiude, tsenderlongitude;

    String transaction_url = "http://" + ipurl + "/PaymentGatewayApi/api/acquirerbank.php?";

    String add_transaction_url = "http://" + ipurl + "/PaymentGatewayApi/addtransactionhistory.php?";

    RequestQueue requestQueue;

    SharedPreferences sharedpreferences;

    RadioButton rbankbtn, rwalletbtn;
    String rpaymethod;


    /** Called when sample is created. Displays generic UI with welcome text. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        if (v != null) {
            mAccountField = (TextView) v.findViewById(R.id.card_account_field);
            mAccountField.setText("Waiting...");

            requestQueue = Volley.newRequestQueue(getActivity());

            mLoyaltyCardReader = new LoyaltyCardReader(this);

            // Disable Android Beam and register our card reader callback
            enableReaderMode();

        }

        rbankbtn = (RadioButton) v.findViewById(R.id.rbankRadio);
        rwalletbtn = (RadioButton) v.findViewById(R.id.rwalletRadio);

        //sharedpreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        /*if(rbankbtn.isChecked()){
            rpaymethod = "bank";
            userpanno = sharedpreferences.getString("Pan", "");
        }
        if(rwalletbtn.isChecked()){
            rpaymethod = "wallet";
            userpanno = "1478258936982587";
        }

        rbankbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                rpaymethod = "bank";
                userpanno = sharedpreferences.getString("Pan", "");
            }
        });

        rwalletbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                rpaymethod = "wallet";
                userpanno = "1478258936982587";

            }
        });*/

        return v;



    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.enableReaderMode(activity, mLoyaltyCardReader, READER_FLAGS, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.disableReaderMode(activity);
        }
    }

    @Override
    public void onAccountReceived(final String account) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAccountField.setText(account);

                try {
                    JSONObject jsonObject = new JSONObject(account);

                    //jsonObject.getString("senderid");

                    sharedpreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    requestToken = jsonObject.getString("token");
                    transactionToken = jsonObject.getString("tokenrequestid");
                    senderid = jsonObject.getString("userid");
                    senderaccno = jsonObject.getString("accountNumber");
                    amt = jsonObject.getString("amount");
                    spaymentmethod = "bank";
                                //jsonObject.getString("spaymentMethod");
                    sscore = jsonObject.getString("score");
                    tsenderlatitiude = "";
                          //  jsonObject.getString("senderlatitude");
                    tsenderlongitude = "";
                            //jsonObject.getString("senderlongitude");
                    userid = sharedpreferences.getString("UserId", "");

                    if(rbankbtn.isChecked()){
                        rpaymethod = "bank";
                        userpanno = sharedpreferences.getString("Pan", "");
                    }
                    if(rwalletbtn.isChecked()){
                        rpaymethod = "wallet";
                        userpanno = "1478258936982587";
                    }

                    rbankbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            rpaymethod = "bank";
                            userpanno = sharedpreferences.getString("Pan", "");
                        }
                    });

                    rwalletbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            rpaymethod = "wallet";
                            userpanno = "1478258936982587";

                        }
                    });


                    transaction_url = transaction_url + "tokenRequestId=" + transactionToken + "&token=" + requestToken + "&senderid=" + senderid + "&senderaccno=" + senderaccno + "&amt=" + amt + "&ruserid=" + userid + "&rpanno=" + userpanno + "&spaymentMethod=" + spaymentmethod + "&rpaymentMethod=" + rpaymethod;

                    Log.i("user Response" ,"user " + transaction_url);

                    mAccountField.setText(amt);

                    makeTransaction();


                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    public void makeTransaction(){

        StringRequest transactionRequest = new StringRequest(Request.Method.GET, transaction_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Transaction Response" ,"payment " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.i("Transaction Response" , response);

                    res = jsonObject.getString("error");

                    if(res.equals("true")){

                        Intent failIntent = new Intent(getActivity(), PaymentStatusActivity.class);
                        failIntent.putExtra("status", "failed");
                        startActivity(failIntent);

                    } else {

                        transactionAmt = jsonObject.getString("amount");
                        sAccNo = jsonObject.getString("senderAccNo");
                        rAccNo = jsonObject.getString("raccountNumber");
//                        ttime = jsonObject.getString("transactionTime");
                        tsendername = jsonObject.getString("senderName");
                        trecievername = jsonObject.getString("recieverName");

                        addTransactionHistory();

                        Intent passIntent = new Intent(getActivity(), PaymentStatusActivity.class);
                        passIntent.putExtra("status", "success");
                        passIntent.putExtra("Amount", transactionAmt);
                        passIntent.putExtra("senderAcc", sAccNo);
                        passIntent.putExtra("receiverAcc", rAccNo);
                        passIntent.putExtra("senderName", tsendername);
                        passIntent.putExtra("receiverName", trecievername);
                        startActivity(passIntent);



                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });

        requestQueue.add(transactionRequest);

    }




    public void addTransactionHistory(){



        add_transaction_url = add_transaction_url + "sid=" + senderid + "&rid=" + userid + "&saccno=" + sAccNo + "&raccno=" + rAccNo + "&amt=" + transactionAmt + "&latitiude=" + tsenderlatitiude + "&longitude=" + tsenderlongitude + "&score=" + sscore;


        //Toast.makeText(getActivity(), add_transaction_url, Toast.LENGTH_SHORT).show();

        StringRequest addTransactionRequest = new StringRequest(Request.Method.GET, add_transaction_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Volley Error: ", String.valueOf(error));

            }
        });

        requestQueue.add(addTransactionRequest);

    }

}
