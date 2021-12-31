package com.sanktest.mytestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentStatusActivity extends AppCompatActivity {

    ImageView imageView;

    TextView tv1, tv2, tv3, tv4, tv5, tv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        getSupportActionBar().hide();

        tv1 = (TextView) findViewById(R.id.paymentStatus);
        tv2 = (TextView) findViewById(R.id.paymentAmt);
        tv3 = (TextView) findViewById(R.id.paymentSenderAcc);
        tv4 = (TextView) findViewById(R.id.paymentReceiverAcc);
        tv5 = (TextView) findViewById(R.id.paymentName);
        tv6 = (TextView) findViewById(R.id.paymentRecName);
        imageView = (ImageView) findViewById(R.id.statusImg);

        String payStatus = getIntent().getStringExtra("status");

        if(payStatus.equals("success")){

            imageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));
            tv1.setText("PAYMENT SUCCESSFUL");
            tv2.setText("\u20B9"+getIntent().getStringExtra("Amount"));
            tv3.setText("Sender A/C No: "+getIntent().getStringExtra("senderAcc"));
            tv4.setText("Receiver A/C No: "+getIntent().getStringExtra("receiverAcc"));
            tv5.setText("Name: "+getIntent().getStringExtra("senderName"));
            tv6.setText("Name: "+getIntent().getStringExtra("receiverName"));

        } else {
            startActivity(new Intent(PaymentStatusActivity.this, PaymentFailed.class));
//            imageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_error));
//            tv1.setText("Payment Failed");
//            tv2.setText("");
//            tv3.setText("");
//            tv4.setText("");
//            /*tv2.setText("\u20B9"+getIntent().getStringExtra("Amount"));
//            tv3.setText("Sender A/C No: "+getIntent().getStringExtra("senderAcc"));
//            tv4.setText("Receiver A/C No: "+getIntent().getStringExtra("receiverAcc"));*/
        }

    }


    public void backToMain(View view){

        startActivity(new Intent(PaymentStatusActivity.this, ChooseActivity.class));

    }

}
