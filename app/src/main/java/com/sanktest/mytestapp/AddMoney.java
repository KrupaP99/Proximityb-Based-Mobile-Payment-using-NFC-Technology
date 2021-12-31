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
import com.sanktest.mytestapp.logger.Log;

import static com.sanktest.mytestapp.LoginActivity.ipurl;

public class AddMoney extends AppCompatActivity {

    String addmoneyurl = "http://"+ipurl+"/PaymentGatewayApi/api/addmoney.php?pan=";

    EditText addamount;

    String panNo, panExpiryMth, panExpiryYr, userid;
    String imei,model;

    RequestQueue requestQueue;

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    String urlEncoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        addamount = (EditText) findViewById(R.id.addamt);

        panNo = getIntent().getStringExtra("panNo");
        panExpiryMth = getIntent().getStringExtra("panExpiryMth");
        panExpiryYr = getIntent().getStringExtra("panExpiryYr");
        userid = getIntent().getStringExtra("userid");
        imei = getIntent().getStringExtra("imei");
        model = getIntent().getStringExtra("model");

        requestQueue = Volley.newRequestQueue(this);
    }

    public void addmoneynow(View view) {

        addmoneyurl = addmoneyurl + panNo +"&panExpMonth="+ panExpiryMth +"&panExpYear="+ panExpiryYr +"&imei=" + imei + "&model="+ model + "&userId="+ userid +"&amt=";

        addmoneyurl = addmoneyurl + addamount.getText();

        urlEncoded = Uri.encode(addmoneyurl, ALLOWED_URI_CHARS);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEncoded, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("success")){

                    startActivity(new Intent(AddMoney.this, ChooseActivity.class));
                    Toast.makeText(AddMoney.this, "Money Added To Wallet",Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(AddMoney.this, response, Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(AddMoney.this, String.valueOf(error), Toast.LENGTH_LONG).show();

            }
        });


        requestQueue.add(stringRequest);

    }

}
