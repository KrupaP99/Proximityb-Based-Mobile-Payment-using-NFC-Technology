package com.sanktest.mytestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Admin on 28-03-2018.
 */

public class PaymentFailed  extends AppCompatActivity{


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);
        getSupportActionBar().hide();


    }

    public void backToMain(View view){

        startActivity(new Intent(PaymentFailed.this, ChooseActivity.class));

    }
}
