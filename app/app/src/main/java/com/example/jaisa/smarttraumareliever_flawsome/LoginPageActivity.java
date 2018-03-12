package com.example.jaisa.smarttraumareliever_flawsome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jaisa.smarttraumareliever_flawsome.Helpers.DBHelper;
import com.example.jaisa.smarttraumareliever_flawsome.Helpers.SPHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

public class LoginPageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Button verifyPhoneButton,sendSMSButton;
    private EditText phoneText,usernameText;
    private EditText verifies[] = new EditText[6];
    private String enteredVerificationCode = "******",veriId,uid,phone;
    private ProgressDialog progressDialog;
    private LinearLayout verificationCode;
    public int noOfValuesInSharedPref =0;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        progressDialog = new ProgressDialog(this);
        verifyPhoneButton = findViewById(R.id.verifyPhoneButton);
        sendSMSButton = findViewById(R.id.sendSMSButton);
        phoneText = findViewById(R.id.phoneText);
        verificationCode = findViewById(R.id.verification_code);
        usernameText = findViewById(R.id.usernameText);
        verificationCode.setVisibility(View.INVISIBLE);
        verifyPhoneButton.setEnabled(false);
        verifyPhoneButton.getBackground().setAlpha(70);
        for (int i=0; i<6; i++) {
            int id = getResources().getIdentifier("verify" + (i + 1), "id", getPackageName());
            verifies[i] = findViewById(id);
        }
        for(int i=0; i<6; i++){
            final int j=i;
            verifies[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Toast.makeText(LoginPageActivity.this, verifies[j].getText().toString()+ "\n"+ enteredVerificationCode.substring(0, j) + "\n" + verifies[j].getText() + "\n" + enteredVerificationCode.substring(j + 1, 6), Toast.LENGTH_SHORT).show();
                    if(!verifies[j].getText().toString().equals("")) {
                        enteredVerificationCode = enteredVerificationCode.substring(0, j) + verifies[j].getText() + enteredVerificationCode.substring(j + 1, 6);
                        if (j < 5) {
                            verifies[j + 1].requestFocus();
                        }
                        //Toast.makeText(LoginPageActivity.this, enteredVerificationCode, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser != null){
            Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
            startActivity(intent);
        }
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(LoginPageActivity.this, "Verification Failed! Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                veriId = verificationId;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                Toast.makeText(LoginPageActivity.this, "Verification Pending...",Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        };

        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "+91" + phoneText.getText().toString();
                progressDialog.setMessage("Verifying Phone Number...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if(!usernameText.getText().toString().equals("")) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 7, TimeUnit.SECONDS, LoginPageActivity.this, mCallbacks);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            verifyPhoneButton.setEnabled(true);
                            verifyPhoneButton.getBackground().setAlpha(255);
                            verificationCode.setVisibility(View.VISIBLE);
                        }
                    }, 10000);
                }
                else {
                    Toast.makeText(LoginPageActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        verifyPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(!usernameText.getText().toString().equals("")) {
                    //Toast.makeText(LoginPageActivity.this, veriId+"", Toast.LENGTH_SHORT).show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(veriId, enteredVerificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
                else {
                    Toast.makeText(LoginPageActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Toast.makeText(LoginPageActivity.this,mCurrentUser.getDisplayName()+".."+mCurrentUser.getPhoneNumber(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        mCurrentUser = user;

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    FirebaseUser user = task.getResult().getUser();
                    mCurrentUser = user;
                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(usernameText.getText().toString()).build();
                    mCurrentUser.updateProfile(userProfileChangeRequest);
                    Toast.makeText(LoginPageActivity.this, "Verification Success", Toast.LENGTH_SHORT).show();
                    uid= mCurrentUser.getUid();
                    phone = mCurrentUser.getPhoneNumber();
                    Log.e("id",uid);
                    storeValuesInSharedPref();
                    addUserToDB();
                    Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(LoginPageActivity.this, "Verification Failed! Invalid Code Entered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        progressDialog.hide();
    }
    public void storeValuesInSharedPref()
    {
        int n;
        context = this;
        String noOfUserString = SPHelper.getSP(this,"totalUsers");
        if(noOfUserString.equals("none"))
            n=0;
        else
            n = Integer.parseInt(noOfUserString);
        ++n;
        SPHelper.setSP(this,"uid"+n,uid);
        SPHelper.setSP(this,"phone"+n,phone);
        SPHelper.setSP(this,"username"+n,usernameText.getText().toString());
        SPHelper.setSP(this,"totalUsers",n+"");
        SPHelper.setSP(this,"currentUser",n+"");
        Log.e("user, phone, uid, n",usernameText.getText().toString()+" , "+phone+" , "+uid+" , "+n);
    }
    public void addUserToDB()
    {
        DBHelper.initialize();
        DBHelper.addUser();
    }
}
