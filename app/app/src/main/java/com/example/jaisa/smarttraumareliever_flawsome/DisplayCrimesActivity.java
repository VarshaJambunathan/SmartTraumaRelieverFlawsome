package com.example.jaisa.smarttraumareliever_flawsome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jaisa.smarttraumareliever_flawsome.Adapters.SolvedCrimesAdapter;
import com.example.jaisa.smarttraumareliever_flawsome.Beans.Complaint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisplayCrimesActivity extends AppCompatActivity {

    private RecyclerView mCrimesReportedView;
    private RecyclerView.Adapter mCrimesReportedAdapter;
    private RecyclerView.LayoutManager mCrimesReportedLayoutManager;
    private ArrayList<Complaint> mCrimesReportedList;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_crimes);

        //getSupportActionBar().setTitle("Crimes Reported");

        mCrimesReportedView = (RecyclerView) findViewById(R.id.crimes_reported_view);
        mCrimesReportedView.setHasFixedSize(true);
        mCrimesReportedLayoutManager = new LinearLayoutManager(this);
        mCrimesReportedView.setLayoutManager(mCrimesReportedLayoutManager);

        mCrimesReportedList = new ArrayList<>();
        //mCrimesReportedList.add(new Complaint("Acid", "User's desc", "11/03/2018", "23:27", "Karnataka Police"));
        //mCrimesReportedList.add(new Complaint("Napthalene", "User's desc", "17/01/2018", "03:27", "Kerala Police"));
        //mCrimesReportedList.add(new Complaint("Murder", "User's desc", "02/09/2018", "20:20", "Andhra Police"));

        mCrimesReportedAdapter =  new SolvedCrimesAdapter(mCrimesReportedList);
        mCrimesReportedView.setAdapter(mCrimesReportedAdapter);

        reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCrimesReportedList.clear();
                // Toast.makeText(SolvedCrimesActivity.this, "hello", Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot: dataSnapshot.child("users").getChildren()) {
                    String id = postSnapshot.getKey();
                    //Toast.makeText(SolvedCrimesActivity.this, "id: "+id, Toast.LENGTH_SHORT).show();
                    for(DataSnapshot crimeSnap :dataSnapshot.child("users").child(id).child("crimes").getChildren())
                    {
                        String crimeID= crimeSnap.getKey();
                        //Toast.makeText(SolvedCrimesActivity.this, "crimeID:"+crimeID+"::", Toast.LENGTH_SHORT).show();
                        String desc= dataSnapshot.child("users").child(id).child("crimes").child(crimeID).child("description").getValue(String.class);
                        //Toast.makeText(SolvedCrimesActivity.this, "description:"+desc+"::", Toast.LENGTH_SHORT).show();
                        /*Log.e("description", desc+"gfghf");
                        Log.i("Idescription", desc+"jhu");*/
                        String reportedTo= dataSnapshot.child("users").child(id).child("crimes").child(crimeID).child("reportedTo").getValue(String.class);
                        Boolean solved = dataSnapshot.child("users").child(id).child("crimes").child(crimeID).child("solvedDetails").child("solved").getValue(Boolean.class);
                        long solvedTime = dataSnapshot.child("users").child(id).child("crimes").child(crimeID).child("solvedDetails").child("solvedTimestamp").getValue(Long.class);
                        Date dates = new Date(solvedTime*1000L);
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                        DateFormat time = new SimpleDateFormat("hh:mm:ss a");
                        // Log.e("solved", solved+"");
                        //Log.i("Isolved", solved+"");
                        // Toast.makeText(SolvedCrimesActivity.this, "description:"+desc+"::"+"reportedTo:"+reportedTo+"::solved"+solved+"::solvedTime:"+solvedTime, Toast.LENGTH_SHORT).show();
                        /*int date = crimeSnap.child(crimeID).child("reportedTimestamp").child("date").getValue(Integer.class);
                        int month = crimeSnap.child(crimeID).child("reportedTimestamp").child("month").getValue(Integer.class);
                        int hour = crimeSnap.child(crimeID).child("reportedTimestamp").child("hours").getValue(Integer.class);
                        int minute = crimeSnap.child(crimeID).child("reportedTimestamp").child("minutes").getValue(Integer.class);
                        int sec = crimeSnap.child(crimeID).child("reportedTimestamp").child("seconds").getValue(Integer.class);
                        String t = hour+":"+minute+":"+sec;*/
                        if(!solved) {
                            mCrimesReportedList.add(new Complaint(crimeID, desc, date.format(dates),time.format(dates), reportedTo));
                            // Toast.makeText(SolvedCrimesActivity.this, desc+"...", Toast.LENGTH_SHORT).show();
                            //mSolvedList.notify();
                            mCrimesReportedAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
