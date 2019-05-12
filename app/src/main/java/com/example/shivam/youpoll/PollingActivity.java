package com.example.shivam.youpoll;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class PollingActivity extends AppCompatActivity {


    EditText ques;
    EditText ch1;
    EditText ch2;
    EditText ch3;
    EditText ch4;
    Button savePoll;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference mDatabaseRef;

    Integer counter = 0;
    ArrayList<String> choices = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer c = dataSnapshot.getValue(Integer.class);
                counter = c;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        savePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
    }

    public void init() {
        ques = findViewById(R.id.ques_polling);
        ch1 = findViewById(R.id.choice1_polling);
        ch2 = findViewById(R.id.choice2_polling);
        ch3 = findViewById(R.id.choice3_polling);
        ch4 = findViewById(R.id.choice4_polling);

        savePoll = findViewById(R.id.save_poll);
    }

    public void sendData() {

        choices.add(ch1.getText().toString().trim());
        choices.add(ch2.getText().toString().trim());
        choices.add(ch3.getText().toString().trim());
        choices.add(ch4.getText().toString().trim());


        mDatabaseRef.child("Polls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.hasChild("Total")) {
                        String i = dataSnapshot.child("Total").getValue(String.class);
                        counter = Integer.valueOf(i);
                        counter++;
                        Log.d("FF", "counter is " + counter);
                        Log.d("FF", "i is " + i);

                    } else {
                        counter = 0;
                    }
                    saveData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void saveData() {

        mDatabaseRef = database.getReference().child("Polls").child(counter.toString());

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("Count", counter.toString());
        userMap.put("Question", ques.getText().toString().trim());
        userMap.put("a", ch1.getText().toString().trim());
        userMap.put("b", ch2.getText().toString().trim());
        userMap.put("c", ch3.getText().toString().trim());
        userMap.put("d", ch4.getText().toString().trim());
        userMap.put("av", "0");
        userMap.put("bv", "0");
        userMap.put("cv", "0");
        userMap.put("dv", "0");
        mDatabaseRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("FF", "SSSSSSUCESSS");


                    mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                if (dataSnapshot.hasChild("Count")) {
                                    String x = dataSnapshot.child("Count").getValue(String.class);
                                    database.getReference().child("Polls").child("Total").setValue(x);
                                    Log.d("FF", " SECOND   SSSSSSUCESSS");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Log.d("FF", "Failed");
                }
            }
        });

        this.finish();
    }
}
