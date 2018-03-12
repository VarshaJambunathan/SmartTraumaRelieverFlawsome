package com.example.jaisa.smarttraumareliever_flawsome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FIRReportActivity extends AppCompatActivity {
    private ArrayList<String> laws, descriptions;
    private String additionalComments, incident, lawText="";
    private TextView additionalCommentsTextView, incidentTextView, lawsViolatedTextView;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firreport);

        laws = getIntent().getExtras().getStringArrayList("laws");
        descriptions = getIntent().getExtras().getStringArrayList("descriptions");
        additionalComments = getIntent().getExtras().getString("comments");
        incident = getIntent().getExtras().getString("incident");
        for(int i=0; i<laws.size(); i++){
            lawText = lawText+(i+1)+". "+laws.get(i)+"\n";
        }

        lawsViolatedTextView = findViewById(R.id.lawsViolatedTextBox);
        incidentTextView = findViewById(R.id.incidentTextBox);
        additionalCommentsTextView = findViewById(R.id.additionalCommentsTextBox);
        homeButton = findViewById(R.id.homeButton);

        lawsViolatedTextView.setText(lawText);
        incidentTextView.setText(incident);
        additionalCommentsTextView.setText(additionalComments);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FIRReportActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
