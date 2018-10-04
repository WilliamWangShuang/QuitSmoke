package com.example.william.quitsmokeappclient.Fragments;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;
import clientservice.factory.GameView;

public class GameFragment extends Fragment {
    //declaring gameview
    private GameView gameView;
    private Context mContext;

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Getting display object
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        //Getting the screen resolution into point object
        final Point size = new Point();
        display.getSize(size);

        //Initializing game view object
        gameView = new GameView(mContext, size.x, size.y);

        // initialize restart button
        Button btnRestart = new Button(mContext);
        btnRestart.setText("Restart");
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.reset();
                gameView.resume();
            }
        });
        // initialize go back game home page button
        Button btnBack = new Button(mContext);
        btnBack.setText("Back");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new GameMainFragment()).commit();;
            }
        });
        FrameLayout game = new FrameLayout(mContext);
        LinearLayout gameWidgets = new LinearLayout(mContext);
        gameWidgets.addView(btnRestart);
        gameWidgets.addView(btnBack);
        game.addView(gameView);
        game.addView(gameWidgets);
        return game;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //pausing the game when activity is paused
    @Override
    public void onPause() {
        super.onPause();
        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    public void onResume() {
        super.onResume();
        gameView.resume();
    }

}
