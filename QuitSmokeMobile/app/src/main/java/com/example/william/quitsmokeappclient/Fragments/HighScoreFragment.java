package com.example.william.quitsmokeappclient.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.R;

public class HighScoreFragment extends Fragment {
    private TextView textView,textView2,textView3,textView4;
    private SharedPreferences sharedPreferences;
    private View vHighScoreFragment;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHighScoreFragment = inflater.inflate(R.layout.high_score, container, false);

        return vHighScoreFragment;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        //initializing the textViews
        textView = (TextView)v.findViewById(R.id.textView);
        textView2 = (TextView)v.findViewById(R.id.textView2);
        textView3 = (TextView)v.findViewById(R.id.textView3);
        textView4 = (TextView)v.findViewById(R.id.textView4);

        sharedPreferences  = getActivity().getSharedPreferences("high score", Context.MODE_PRIVATE);

        //setting the values to the textViews
        textView.setText("1# "+sharedPreferences.getInt("score1",0));
        textView2.setText("2# "+sharedPreferences.getInt("score2",0));
        textView3.setText("3# "+sharedPreferences.getInt("score3",0));
        textView4.setText("4# "+sharedPreferences.getInt("score4",0));
    }
}
