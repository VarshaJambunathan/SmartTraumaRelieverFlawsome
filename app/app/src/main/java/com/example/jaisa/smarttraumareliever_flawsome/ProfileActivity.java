package com.example.jaisa.smarttraumareliever_flawsome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    ImageView im1,im2;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;
    private Button logoutButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfileActivity.this, mCurrentUser.getDisplayName() + "....." + mCurrentUser.getPhoneNumber()+"....\nSigning Out...", Toast.LENGTH_SHORT).show();
                mCurrentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(ProfileActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                        logout();
                    }
                });
            }
        });
/*
        im1= findViewById(R.id.profileIcon);
        im2= findViewById(R.id.trackImage);
        t1= findViewById(R.id.name);
        t2= findViewById(R.id.nameValue);
        t3= findViewById(R.id.phone);
        t4= findViewById(R.id.phoneValue);
        t5= findViewById(R.id.reportedCrimeNumber);
        t6= findViewById(R.id.reportedCrimeText);
        t7= findViewById(R.id.solvedCrimeNumber);
        t8= findViewById(R.id.solvedCrimeText);
        t9= findViewById(R.id.trackCrimeText);*/


    }
    private void logout(){
        mAuth.signOut();
        Toast.makeText(ProfileActivity.this, "Sign Out Successful", Toast.LENGTH_SHORT).show();
        mCurrentUser = null;
        Intent intent = new Intent(ProfileActivity.this, LoginPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
