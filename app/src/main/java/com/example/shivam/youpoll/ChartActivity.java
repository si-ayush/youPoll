package com.example.shivam.youpoll;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    ProgressBar progressBar;
    BarChart barChart;
    pollingRaw pp;
    FirebaseDatabase database;
    DatabaseReference mRef;
    Integer pos;
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar_cyclic);
        progressBar.setVisibility(View.VISIBLE);
        database = FirebaseDatabase.getInstance();
        barChart = findViewById(R.id.bar_chart);

        Bundle bundle = getIntent().getExtras();
        pos = bundle.getInt("Poll");
        getPoll(pos);


    }

    public void setBarChart() {

        setEntriesLabels();
        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data);

        barChart.setDescription("Votes");


        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);
        progressBar.setVisibility(View.GONE);

    }

    public void setEntriesLabels() {
        entries.add(new BarEntry(Float.parseFloat(pp.getAv()), 0));
        entries.add(new BarEntry(Float.parseFloat(pp.getBv()), 1));
        entries.add(new BarEntry(Float.parseFloat(pp.getCv()), 2));
        entries.add(new BarEntry(Float.parseFloat(pp.getDv()), 3));

        labels.add(pp.getA());
        labels.add(pp.getB());
        labels.add(pp.getC());
        labels.add(pp.getD());
    }

    public void getPoll(Integer x) {
        final Integer yy = x;
        mRef = database.getInstance().getReference();

        mRef.child("Polls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.d("KK", "Getting polls");
                    if (dataSnapshot.hasChild(String.valueOf(yy))) {
                        pp = dataSnapshot.child(String.valueOf(yy)).getValue(pollingRaw.class);
                        Log.d("Chart", pp.getQuestion());
                        setBarChart();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
}
