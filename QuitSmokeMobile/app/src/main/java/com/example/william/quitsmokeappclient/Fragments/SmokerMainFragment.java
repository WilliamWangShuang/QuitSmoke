package com.example.william.quitsmokeappclient.Fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.william.quitsmokeappclient.R;

import clientservice.QuitSmokeClientUtils;
import clientservice.factory.GetCurrentPlanFactorial;

public class SmokerMainFragment extends Fragment {
    private CircleProgressBar mCustomProgressBar;
    private Button btnPanicButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.smoker_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set progress bar
        mCustomProgressBar = (CircleProgressBar)view.findViewById(R.id.custom_progress);
        // get current proceeding plan
        GetCurrentPlanFactorial getCurrentPlanFactorial = new GetCurrentPlanFactorial(getActivity(), QuitSmokeClientUtils.getUid(), mCustomProgressBar);
        getCurrentPlanFactorial.execute();

        // get panic button
        btnPanicButton = (Button)view.findViewById(R.id.buttonforquitter);
        btnPanicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
