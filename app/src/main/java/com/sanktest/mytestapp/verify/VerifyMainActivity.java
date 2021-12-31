package com.sanktest.mytestapp.verify;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sanktest.mytestapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyMainActivity extends AppCompatActivity {
    private final static String API_KEY = "6e30953c-4453-11eb-8153-0200cd936042";
    private Button btnSubmit;
    private EditText  mobileNumber,etOTP;
    private String sessionId,otp,otpAuto;
    public static final String OTP_REGEX = "[0-9]{1,6}";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
   // private ChangeListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_verify);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        etOTP=(EditText)findViewById(R.id.etOTP);

        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.

            if (API_KEY.isEmpty()) {

                Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from 2Factor.in", Toast.LENGTH_LONG).show();
                return;
            }

        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mobileNumber.getText().toString())) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(VerifyMainActivity.this);
                    builder.setMessage("Enter all the information requested").setPositiveButton("Dismiss", dialogClickListener)
                            .show();

                } else {
                    // Check if no view has focus:
                    View view1 = getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    };
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<MessageResponse> call = apiService.sentOTP(API_KEY, mobileNumber.getText().toString());
                    call.enqueue(new Callback<MessageResponse>() {
                        @Override
                        public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                            sessionId = response.body().getDetails();
                            Log.d("SenderID", sessionId);
                             getOTP();
                            etOTP
                                    .setVisibility(View.VISIBLE);


                        }

                        @Override
                        public void onFailure(Call<MessageResponse> call, Throwable t) {
                            Log.e("ERROR", t.toString());
                        }

                    });
                }

            }
        });



        etOTP.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 6)
                {
                    if(etOTP.getText()!=null){
                        otpAuto = etOTP.getText().toString();
                        checkOtp();
                    }
                }
            }
        });
    }

    private void getOTP() {

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

                //From the received text string you may do string operations to get the required OTP
                //It depends on your SMS format
                // If your OTP is six digits number, you may use the below code

             /*   Pattern pattern = Pattern.compile(OTP_REGEX);
                Matcher matcher = pattern.matcher(messageText);

                while (matcher.find())
                {
                    otp1 = matcher.group();
                }
                */
                otpAuto=messageText.substring(0,6);
                etOTP.setText(otpAuto);
            }
        });
    }

    private void checkOtp() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MessageResponse> call = apiService.verifyOTP(API_KEY,sessionId, otpAuto);

        call.enqueue(new Callback<MessageResponse>() {

            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                try {
                    if(response.body().getStatus().equals("Success")){
                    Intent i=new Intent(VerifyMainActivity.this,SuccessPage.class);
                    startActivity(i);
                    }else{
                        Log.d("Failure", response.body().getDetails()+"|||"+response.body().getStatus());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e("ERROR", t.toString());
            }

        });
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
  /*  public interface ChangeListener {
       public void onChange(String otp);

    }
    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }
*/


}
