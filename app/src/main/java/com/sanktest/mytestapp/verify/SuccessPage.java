package com.sanktest.mytestapp.verify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sanktest.mytestapp.AddAccountActivity;
import com.sanktest.mytestapp.ChooseActivity;
import com.sanktest.mytestapp.LoginActivity;
import com.sanktest.mytestapp.Main2Activity;
import com.sanktest.mytestapp.R;
import com.sanktest.mytestapp.SplashActivity;

/**
 * Created by Sonal Naidu on 23-03-2018.
 */

public class SuccessPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                        startActivity(new Intent(SuccessPage.this, Main2Activity.class));

                    }


            }
        };
        timerThread.start();
    }
}
