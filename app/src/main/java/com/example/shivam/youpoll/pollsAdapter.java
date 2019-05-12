package com.example.shivam.youpoll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.shivam.youpoll.constants.CHOICE_1_TAG;
import static com.example.shivam.youpoll.constants.CHOICE_2_TAG;
import static com.example.shivam.youpoll.constants.CHOICE_3_TAG;
import static com.example.shivam.youpoll.constants.CHOICE_4_TAG;

public class pollsAdapter extends RecyclerView.Adapter<pollsAdapter.MyViewHolder> {

    private ArrayList<pollingRaw> p = new ArrayList<>();
    private saveVote saveVote;
    private openDetailChart openDetailChart;
    private Integer previousButtonId;
    private RadioGroup radioGroup;


    public pollsAdapter(ArrayList<pollingRaw> p, Context c) {

        this.p = p;
        saveVote = (com.example.shivam.youpoll.saveVote) c;
        openDetailChart = (com.example.shivam.youpoll.openDetailChart) c;
    }

    public void clearAdapter() {
        p.clear();
        notifyDataSetChanged();
        if(radioGroup!=null)
        radioGroup.clearCheck();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.polls_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pollingRaw pp = p.get(position);
        holder.ques.setText(pp.getQuestion());
        holder.ch1.setText(pp.getA());
        holder.ch2.setText(pp.getB());
        holder.ch3.setText(pp.getC());
        holder.ch4.setText(pp.getD());

        holder.v1.setText(pp.getAv());
        holder.v2.setText(pp.getBv());
        holder.v3.setText(pp.getCv());
        holder.v4.setText(pp.getDv());


        holder.ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("CC", "CLICKED " + "CH1" + position);
                    saveVote.saveVote(position, CHOICE_1_TAG);
                    for (int f = 0; f < holder.rGroup.getChildCount(); f += 2) {
                        ((RadioButton) holder.rGroup.getChildAt(f)).setEnabled(false);
                    }
                    int a = Integer.valueOf(holder.v1.getText().toString());
                    a++;
                    holder.v1.setText(String.valueOf(a));
                    setVisibility(holder);
                }
            }
        });
        holder.ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("CC", "CLICKED " + "CH2" + position);
                    saveVote.saveVote(position, CHOICE_2_TAG);
                    for (int f = 0; f < holder.rGroup.getChildCount(); f += 2) {
                        ((RadioButton) holder.rGroup.getChildAt(f)).setEnabled(false);
                    }
                    int a = Integer.valueOf(holder.v2.getText().toString());
                    a++;
                    holder.v2.setText(String.valueOf(a));
                    setVisibility(holder);
                }
            }
        });
        holder.ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("CC", "CLICKED " + "CH3" + position);
                    saveVote.saveVote(position, CHOICE_3_TAG);
                    for (int f = 0; f < holder.rGroup.getChildCount(); f += 2) {
                        ((RadioButton) holder.rGroup.getChildAt(f)).setEnabled(false);
                    }
                    int a = Integer.valueOf(holder.v3.getText().toString());
                    a++;
                    holder.v3.setText(String.valueOf(a));
                    setVisibility(holder);
                }
            }
        });
        holder.ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("CC", "CLICKED " + "CH4" + position);
                    saveVote.saveVote(position, CHOICE_4_TAG);
                    for (int f = 0; f < holder.rGroup.getChildCount(); f += 2) {
                        ((RadioButton) holder.rGroup.getChildAt(f)).setEnabled(false);
                    }
                    int a = Integer.valueOf(holder.v4.getText().toString());
                    a++;
                    holder.v4.setText(String.valueOf(a));
                    setVisibility(holder);
                }
            }
        });

        holder.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButtonId = holder.rGroup.getCheckedRadioButtonId();
                holder.rGroup.clearCheck();
                for (int f = 0; f < holder.rGroup.getChildCount(); f += 2) {
                    ((RadioButton) holder.rGroup.getChildAt(f)).setEnabled(true);
                }
                sendPreviousButtonId(holder,position, previousButtonId);
            }
        });

        holder.cardRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailChart.openBarGraph(position);
            }
        });

    }

    public void sendPreviousButtonId(MyViewHolder h, Integer p, Integer id) {
        switch (id) {
            case R.id.ch1_single:

                saveVote.removePreviousVote(p, CHOICE_1_TAG);
                break;
            case R.id.ch2_single:

                saveVote.removePreviousVote(p, CHOICE_2_TAG);
                break;
            case R.id.ch3_single:

                saveVote.removePreviousVote(p, CHOICE_3_TAG);
                break;
            case R.id.ch4_single:

                saveVote.removePreviousVote(p, CHOICE_4_TAG);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return p.size();
    }

    public void setDisable(MyViewHolder holder) {
        for (int f = 0; f < holder.rGroup.getChildCount(); f += 2) {
            ((RadioButton) holder.rGroup.getChildAt(f)).setEnabled(false);
        }
    }

    public void setVisibility(MyViewHolder h) {
        h.v1.setVisibility(View.VISIBLE);
        h.v2.setVisibility(View.VISIBLE);
        h.v3.setVisibility(View.VISIBLE);
        h.v4.setVisibility(View.VISIBLE);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView ques;
        public RadioGroup rGroup;
        public RadioButton ch1, ch2, ch3, ch4;
        public TextView v1, v2, v3, v4;
        public ImageButton refresh;
        public RelativeLayout cardRelative;

        public MyViewHolder(View itemView) {
            super(itemView);
            ques = (TextView) itemView.findViewById(R.id.question_single);
            rGroup = (RadioGroup) itemView.findViewById(R.id.r_group_single);
            ch1 = (RadioButton) itemView.findViewById(R.id.ch1_single);
            ch2 = (RadioButton) itemView.findViewById(R.id.ch2_single);
            ch3 = (RadioButton) itemView.findViewById(R.id.ch3_single);
            ch4 = (RadioButton) itemView.findViewById(R.id.ch4_single);

            v1 = (TextView) itemView.findViewById(R.id.ch1_vote_single);
            v2 = (TextView) itemView.findViewById(R.id.ch2_vote_single);
            v3 = (TextView) itemView.findViewById(R.id.ch3_vote_single);
            v4 = (TextView) itemView.findViewById(R.id.ch4_vote_single);

            refresh = (ImageButton) itemView.findViewById(R.id.refresh_polling);
            cardRelative = (RelativeLayout) itemView.findViewById(R.id.card_relative_single);
            radioGroup = rGroup;
        }
    }

}
