package com.example.william.quitsmokeappclient.Fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.william.quitsmokeappclient.R;
import android.widget.ImageButton;

public class GameMainFragment extends Fragment {
    private View vCalculateFrsFragment;
    //image button
    private ImageButton btnPlay;
    private ImageButton btnHighScore;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vCalculateFrsFragment = inflater.inflate(R.layout.game_layout, container, false);

        return vCalculateFrsFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getting the button
        btnPlay = (ImageButton)view.findViewById(R.id.buttonPlay);
        btnHighScore = (ImageButton)view.findViewById(R.id.buttonScore);

        //adding a click listener
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting game activity
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new GameFragment()).commit();
            }
        });
        btnHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting game activity
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new HighScoreFragment()).commit();
            }
        });
    }
}
