package com.example.jaisa.smarttraumareliever_flawsome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout reportingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setSubtitle(R.string.app_bytext);

        reportingStatus = (LinearLayout)findViewById(R.id.reporting_status);
        reportingStatus.setVisibility(View.GONE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_user_profile){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.report_crime:
                intent = new Intent(MainActivity.this,ReportCrimeActivity.class);
                startActivity(intent);
                break;
            case R.id.crimes_reported:
                intent = new Intent(MainActivity.this,DisplayCrimesActivity.class);
                startActivity(intent);
                break;
            case R.id.solved_crime:
                intent = new Intent(MainActivity.this,SolvedCrimesActivity.class);
                startActivity(intent);
                break;
        }
    }
}
