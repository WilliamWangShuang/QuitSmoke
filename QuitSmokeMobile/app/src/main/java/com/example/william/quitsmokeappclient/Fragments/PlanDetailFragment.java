package com.example.william.quitsmokeappclient.Fragments;

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
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;
import clientservice.QuitSmokeClientUtils;
import clientservice.factory.GetCurrentPlanFactorial;

public class PlanDetailFragment extends Fragment implements IUpdatePartnerAsyncResponse {
    private CircleProgressBar mCustomProgressBar;
    private Button btnUpdateEncourage;
    private String realAmountMsg;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get uid for this plan
        uid = getArguments().getString("uid");
        // set progress bar
        mCustomProgressBar = (CircleProgressBar)view.findViewById(R.id.custom_plan_detail_progress);
        // get current proceeding plan
        GetCurrentPlanFactorial getCurrentPlanFactorial = new GetCurrentPlanFactorial(getActivity(), uid, mCustomProgressBar);
        getCurrentPlanFactorial.delegate = this;
        getCurrentPlanFactorial.execute();

        // get panic button
        btnUpdateEncourage = (Button)view.findViewById(R.id.btn_update_encouragement);
        btnUpdateEncourage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get real amount message
                Toast.makeText(getContext(), "Coming soon..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void processFinish(String reponseResult) {
        realAmountMsg = reponseResult;
    }
}
