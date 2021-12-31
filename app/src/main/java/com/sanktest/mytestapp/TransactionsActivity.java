package com.sanktest.mytestapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sanktest.mytestapp.LoginActivity.ipurl;

public class TransactionsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<AccountUtils> personUtilsList;

    RequestQueue rq;

    SharedPreferences sharedpreferences;
    String userid;

    String request_url = "http://"+ipurl+"/PaymentGatewayApi/gettransactionhistory.php?uid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        rq = Volley.newRequestQueue(this);

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("UserId", "");

        request_url = request_url + userid;

        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        personUtilsList = new ArrayList<>();

        sendRequest();
    }

    public void sendRequest(){



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){

                    AccountUtils personUtils = new AccountUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        personUtils.setSenderid(jsonObject.getString("senderid"));
                        personUtils.setReceiverid(jsonObject.getString("receiverid"));
                        personUtils.setSenderaccno(jsonObject.getString("senderaccno"));
                        personUtils.setReceiveraccno(jsonObject.getString("receiveraccno"));
                        personUtils.setSendername(jsonObject.getString("sendername"));
                        personUtils.setReceivername(jsonObject.getString("receivername"));
                        personUtils.setAmt(jsonObject.getString("amt"));
                        personUtils.setSenderdevicename(jsonObject.getString("senderdevicename"));
                        personUtils.setReceiverdevicename(jsonObject.getString("receiverdevicename"));
                        personUtils.setThtime(jsonObject.getString("thtime"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    personUtilsList.add(personUtils);

                }

                mAdapter = new TransactionAdapter(TransactionsActivity.this, personUtilsList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", String.valueOf(error));
            }
        });

        rq.add(jsonArrayRequest);

    }

}
