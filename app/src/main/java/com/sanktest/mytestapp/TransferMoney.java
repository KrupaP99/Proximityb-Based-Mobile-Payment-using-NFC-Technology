package com.sanktest.mytestapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static com.sanktest.mytestapp.LoginActivity.ipurl;

public class TransferMoney extends AppCompatActivity {

    String transfermoneyurl = "http://"+ipurl+"/PaymentGatewayApi/api/transfer.php?pan=";

    EditText transferamount;

    String panNo, panExpiryMth, panExpiryYr, userid;
    String imei,model;

    RequestQueue requestQueue;

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    String urlEncoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        transferamount = (EditText) findViewById(R.id.transferamt);

        panNo = getIntent().getStringExtra("panNo");
        panExpiryMth = getIntent().getStringExtra("panExpiryMth");
        panExpiryYr = getIntent().getStringExtra("panExpiryYr");
        userid = getIntent().getStringExtra("userid");
        imei = getIntent().getStringExtra("imei");
        model = getIntent().getStringExtra("model");

        requestQueue = Volley.newRequestQueue(this);
    }


    public void transfermoneynow(View view) {


            transfermoneyurl = transfermoneyurl + panNo +"&panExpMonth="+ panExpiryMth +"&panExpYear="+ panExpiryYr +"&imei=" + imei + "&model="+ model + "&userId="+ userid +"&amt=";

            transfermoneyurl = transfermoneyurl + transferamount.getText();

            urlEncoded = Uri.encode(transfermoneyurl, ALLOWED_URI_CHARS);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEncoded, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(response.equals("success")){

                        startActivity(new Intent(TransferMoney.this, ChooseActivity.class));
                        Toast.makeText(TransferMoney.this, "Money Transferred To Bank",Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(TransferMoney.this, response, Toast.LENGTH_LONG).show();

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(TransferMoney.this, String.valueOf(error), Toast.LENGTH_LONG).show();

                }
            });


            requestQueue.add(stringRequest);

        }

    }
