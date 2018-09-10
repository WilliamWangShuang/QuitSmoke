package com.example.william.quitsmokeappclient.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.william.quitsmokeappclient.R;

import clientservice.QuitSmokeClientUtils;
import clientservice.factory.AddSmokeAmountFactorial;
import clientservice.factory.GetCurrentPlanFactorial;

public class EncourageFragment extends Fragment {
    private Button btnYes;
    private Button btnNo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_encouragement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get buttons
        btnYes = view.findViewById(R.id.yesforencouragement);
        btnNo = view.findViewById(R.id.noforencouragement);

        // add actions
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SmokerMainFragment()).commit();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSmokeAmountFactorial addSmokeAmountFactorial = new AddSmokeAmountFactorial(QuitSmokeClientUtils.getUid(), getFragmentManager(), getContext());
                addSmokeAmountFactorial.execute();
            }
        });
    }
}
