package com.example.shivam.youpoll;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.shivam.youpoll.constants.CHOICE_1_TAG;
import static com.example.shivam.youpoll.constants.CHOICE_2_TAG;
import static com.example.shivam.youpoll.constants.CHOICE_3_TAG;
import static com.example.shivam.youpoll.constants.CHOICE_4_TAG;

public class MainActivity extends AppCompatActivity implements saveVote, openDetailChart {

    TextView name;
    FloatingActionButton poll;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference mDatabaseRef;
    FirebaseAuth auth;
    FirebaseUser user;


    Integer total;
    ArrayList<pollingRaw> pollsList = new ArrayList<>();
    RecyclerView mRecyclerView;
    pollsAdapter pollsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar_cyclic);

        progressBar.setVisibility(View.VISIBLE);
        poll = (FloatingActionButton) findViewById(R.id.poll_main);
        name = findViewById(R.id.name_main);
        mRecyclerView = findViewById(R.id.polls_recycler_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        pollsAdapter = new pollsAdapter(pollsList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(pollsAdapter);


        database = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        name.setText(i.getStringExtra("name"));

        poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, PollingActivity.class);
                startActivity(myIntent);
                //finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:

                for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                    if (user.getProviderId().equals("facebook.com")) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "Logged out from Facebook", Toast.LENGTH_LONG);
                    } else if (user.getProviderId().equals("google.com")) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "Logged out from Gmail", Toast.LENGTH_LONG);
                    }
                }


                Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(myIntent);
                finish();
                Log.i("Activity", "STARTED");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getAllPolls() {
        pollsList.clear();
       // pollsAdapter.notifyDataSetChanged();
        // pollsAdapter.clearAdapter();
        mDatabaseRef.child("Polls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.d("KK", "Getting Total");
                    if (dataSnapshot.hasChild("Total")) {
                        String tt = dataSnapshot.child("Total").getValue(String.class);
                        total = Integer.valueOf(tt);
                        Log.d("KK", "Total is" + total);

                        for (int i = 0; i <= total; i++) {
                            final int x = i;
                            mDatabaseRef.child("Polls").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Log.d("KK", "Getting polls");
                                        if (dataSnapshot.hasChild(String.valueOf(x))) {
                                            pollingRaw p = dataSnapshot.child(String.valueOf(x)).getValue(pollingRaw.class);
                                            pollsList.add(p);
                                            Log.d("KK", p.Question);
                                            Log.d("KK", "LIST Above" + pollsList.size());
                                            pollsAdapter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        Log.d("KK", "LIST " + pollsList.size());

                        Log.d("KK", "Adapter ");


                    } else {
                        Log.d("KK", "Total not present ");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getUserPolls() {
        Log.d("ooo", "in get userpolls");
        DatabaseReference dt = database.getReference();
        dt.child("Polls").child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    String s = ((DataSnapshot) i.next()).getKey();
                    Log.d("ooo", s);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        getAllPolls();
        getUserPolls();
    }

    @Override
    public void saveVote(Integer x, String choiceTag) {
        String option = null;
        switch (choiceTag) {
            case CHOICE_1_TAG:
                option = "av";
                break;
            case CHOICE_2_TAG:
                option = "bv";
                break;
            case CHOICE_3_TAG:
                option = "cv";
                break;
            case CHOICE_4_TAG:
                option = "dv";
                break;
        }
        final Integer zz = x;
        final String tt = option;

        Log.d("YY", "tt" + tt);
        Log.d("YY", "zz" + x);
        mDatabaseRef.child("Polls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.hasChild(String.valueOf(zz))) {
                        String i = dataSnapshot.child(String.valueOf(zz)).child(tt).getValue(String.class);
                        Integer y = Integer.valueOf(i);
                        y++;
                        database.getReference().child("Polls").child(String.valueOf(zz)).child(tt).setValue(String.valueOf(y));
                        Log.d("YY", "Changed" + y);
                    } else {
                        Log.d("YY", "NOT Changed");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dd = database.getReference();
        dd.child("Users").child(user.getUid()).child(String.valueOf(zz)).child("selected").setValue(tt);
        Toast.makeText(MainActivity.this, "DONE " + pollsList.get(x).getQuestion(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void removePreviousVote(Integer x, String Choice) {
        final Integer zz = x;
        String option = null;
        switch (Choice) {
            case CHOICE_1_TAG:
                option = "av";
                break;
            case CHOICE_2_TAG:
                option = "bv";
                break;
            case CHOICE_3_TAG:
                option = "cv";
                break;
            case CHOICE_4_TAG:
                option = "dv";
                break;
        }
        final String tt = option;
        mDatabaseRef.child("Polls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.hasChild(String.valueOf(zz))) {
                        String i = dataSnapshot.child(String.valueOf(zz)).child(tt).getValue(String.class);
                        Integer y = Integer.valueOf(i);
                        y--;
                        database.getReference().child("Polls").child(String.valueOf(zz)).child(tt).setValue(String.valueOf(y));
                        Log.d("YY", "Changed" + y);
                    } else {
                        Log.d("YY", "NOT Changed");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dd = database.getReference();
        dd.child("Users").child(user.getUid()).child(String.valueOf(zz)).child("selected").removeValue();

    }

    @Override
    public void openBarGraph(Integer pos) {
        Intent myIntent = new Intent(MainActivity.this, ChartActivity.class);
        myIntent.putExtra("Poll", pos);
        startActivity(myIntent);

    }


}
