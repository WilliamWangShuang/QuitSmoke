package com.example.william.quitsmokeappclient.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;

import clientservice.QuitSmokeClientUtils;
import clientservice.factory.GetCurrentPlanFactorial;

public class PlanDetailFragment extends Fragment implements IUpdatePartnerAsyncResponse {
    private CircleProgressBar mCustomProgressBar;
    private Button btnUpdateEncourage;
    private Button btnUpdateMilestone;
    private String realAmountMsg;
    private TextView tvMilestone;
    private FragmentActivity myContext;
    private String uid;
    private String createDT;
    private SetEncouragementDialogFragement setEncouragementDialogFragement;
    private SetMilestoneDialogFragement setMilestoneDialogFragement;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

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
        // get plan create date
        createDT = getArguments().getString("createDT");
        Log.d("QuitSmokeDebug", "uid from previous view:" + uid);
        // get textview milestone
        tvMilestone = (TextView)view.findViewById(R.id.tv_plan_detail_milestone);
        // set progress bar
        mCustomProgressBar = (CircleProgressBar)view.findViewById(R.id.custom_plan_detail_progress);
        // get current proceeding plan
        GetCurrentPlanFactorial getCurrentPlanFactorial = new GetCurrentPlanFactorial(getActivity(), uid, mCustomProgressBar, tvMilestone, null, false);
        getCurrentPlanFactorial.delegate = this;
        getCurrentPlanFactorial.execute();

        // set update encouragement button
        btnUpdateEncourage = (Button)view.findViewById(R.id.btn_update_encouragement);
        btnUpdateEncourage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("smokerUid", uid);
                bundle.putString("createDT", createDT);
                setEncouragementDialogFragement = new SetEncouragementDialogFragement();
                setEncouragementDialogFragement.setArguments(bundle);
                setEncouragementDialogFragement.show(myContext.getSupportFragmentManager(), "updateEncouragement");
            }
        });
        // set update milestone button
        btnUpdateMilestone = (Button)view.findViewById(R.id.btn_set_milestone);
        btnUpdateMilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("smokerUid", uid);
                bundle.putInt("currentPoint", Integer.parseInt(realAmountMsg));
                bundle.putString("createDT", createDT);
                setMilestoneDialogFragement = new SetMilestoneDialogFragement();
                setMilestoneDialogFragement.setArguments(bundle);
                setMilestoneDialogFragement.show(myContext.getSupportFragmentManager(), "updateMilestone");
            }
        });
    }

    @Override
    public void processFinish(String reponseResult) {
        btnUpdateEncourage.setEnabled(true);
        btnUpdateMilestone.setEnabled(true);
        Log.d("QuitSmokeDebug", "real point of this plan from backend:" + reponseResult);
        realAmountMsg = reponseResult;
    }
}
