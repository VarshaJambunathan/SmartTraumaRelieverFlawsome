package com.example.jaisa.smarttraumareliever_flawsome.Helpers;

/**
 * Created by jaisa on 3/11/2018.
 */

import android.util.Log;

import com.example.jaisa.smarttraumareliever_flawsome.Beans.Complaint;
import com.example.jaisa.smarttraumareliever_flawsome.LoginPageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DBHelper {

    static private DatabaseReference myRef;
    ArrayList<Complaint> mSolvedList= new ArrayList<>();

    public static void initialize()
    {
        myRef= FirebaseDatabase.getInstance().getReference();
    }
    private static int generatedCrimeId()
    {
        return (int)(Math.random() * 9999);
    }
    public static void addUser()
    {
        if(SPHelper.getSP(LoginPageActivity.context,"currentUser").equals("none"))
            return;
        else
        {
            int n= Integer.parseInt(SPHelper.getSP(LoginPageActivity.context,"currentUser"));
            String name= SPHelper.getSP(LoginPageActivity.context,"username"+n);
            String phone= SPHelper.getSP(LoginPageActivity.context,"phone"+n);
            String uid= SPHelper.getSP(LoginPageActivity.context,"uid"+n);
            myRef.child("users").child(uid);
            myRef.child("users").child(uid).child("name").setValue(name);
            myRef.child("users").child(uid).child("phone").setValue(phone);
        }
    }
    public static void addCrime(String uid,String desc,String reportedTo)
    {
        initialize();
        int crimeId = generatedCrimeId();
        Timestamp t= new Timestamp(System.currentTimeMillis());
        Boolean val =false;
        myRef.child("users").child(uid).child("crimes").child(crimeId+"").child("reportedTimestamp").setValue(t);
        myRef.child("users").child(uid).child("crimes").child(crimeId+"").child("description").setValue(desc);
        myRef.child("users").child(uid).child("crimes").child(crimeId+"").child("reportedTo").setValue(reportedTo);
        myRef.child("users").child(uid).child("crimes").child(crimeId+"").child("solvedDetails").child("solved").setValue(val);
        myRef.child("users").child(uid).child("crimes").child(crimeId+"").child("solvedDetails").child("solvedTimestamp").setValue(System.currentTimeMillis());


    }
    public static void addSolvedDetails(Boolean solved,String crimeId,String uid)
    {
        initialize();
        Timestamp t= new Timestamp(System.currentTimeMillis());
        myRef.child("users").child(uid).child("crimes").child(crimeId+"").child("solvedDetails").child("solved").setValue(solved);
        myRef.child("users").child(uid).child("crimes").child(crimeId+"").child("solvedDetails").child("solvedTimestamp").setValue(t);
    }
 public ArrayList retreiveSolved()
 {

     return mSolvedList;
 }
}
