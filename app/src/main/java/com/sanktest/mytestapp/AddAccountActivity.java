package com.sanktest.mytestapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static com.sanktest.mytestapp.LoginActivity.ipurl;

public class AddAccountActivity extends AppCompatActivity {

    EditText ETpanno,ETpanexpmth,ETpanexpyr,ETpancvv,ETpanotp, et;

    String userId, pannumber, panexpirymonth, panexpiryyear, pancvv, panotp, url, urlEncoded;

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    RequestQueue requestQueue;

    String otpStr;

    AlertDialog alertDialog;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    //Button btnAddAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        ETpanno = (EditText) findViewById(R.id.urpanno);
        ETpanexpmth = (EditText) findViewById(R.id.urpanmth);
        ETpanexpyr = (EditText) findViewById(R.id.urpanyr);
        ETpancvv = (EditText) findViewById(R.id.urpancvv);
        ETpanotp = (EditText) findViewById(R.id.urpanotp);

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        requestQueue = Volley.newRequestQueue(this);

    }

    public void addAccount(View view){


        userId = sharedpreferences.getString("UserId", "");
        pannumber = ETpanno.getText().toString();
        panexpirymonth = ETpanexpmth.getText().toString();
        panexpiryyear = ETpanexpyr.getText().toString();
        pancvv = ETpancvv.getText().toString();
        panotp = ETpanotp.getText().toString();

        if(!userId.equals("")&&!pannumber.equals("")&&!panexpirymonth.equals("")&&!panexpiryyear.equals("")&&!pancvv.equals("")&&!panotp.equals("")){

            if(pannumber.length() == 16){
                addCardDetails();
            } else {
                Toast.makeText(AddAccountActivity.this, "Invalid PAN Number", Toast.LENGTH_LONG).show();
            }
            //alertBox();



            //Toast.makeText(RegisterActivity.this, "Registered Sucessfully "+url, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(AddAccountActivity.this, "Enter All Values", Toast.LENGTH_LONG).show();
        }

    }


    public void alertBox(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(this);
        tv.setText("ENTER OTP");
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        et = new EditText(this);
        //otpStr = et.getText().toString();

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setTitle("Enter OTP");
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
                otpStr = et.getText().toString();
                addCardDetails();
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

    public void addCardDetails(){

        url = "http://"+ipurl+"/PaymentGatewayApi/adduseraccount.php?uid="+userId+"&panno="+pannumber+"&panexpm="+panexpirymonth+"&panexpy="+panexpiryyear+"&cvv="+pancvv+"&otp="+panotp;
        urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEncoded, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                if(response.equals("success")){

                    editor = sharedpreferences.edit();
                    editor.putString("PrimaryBankAcc", pannumber);
                    editor.putString("Pan", pannumber);
                    editor.putString("ExpMonth", panexpirymonth);
                    editor.putString("ExpYear", panexpiryyear);
                    editor.putString("CVV", pancvv);
                    editor.commit();

                    ETpanno.getText().clear();
                    ETpanexpmth.getText().clear();
                    ETpanexpyr.getText().clear();
                    ETpancvv.getText().clear();
                    //et.getText().clear();

                    pannumber = "";
                    panexpirymonth = "";
                    panexpiryyear = "";
                    pancvv = "";
                    panotp = "";
                    //otpStr = "";

                            //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    Toast.makeText(AddAccountActivity.this, "Account Added Successfully", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(AddAccountActivity.this, ChooseActivity.class));

                } else {
                    Toast.makeText(AddAccountActivity.this, response, Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AddAccountActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue.add(stringRequest);

        stringRequest.setShouldCache(false);
    }

}
