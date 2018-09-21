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
import android.widget.Toast;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;
import clientservice.QuitSmokeClientUtils;
import clientservice.factory.GetCurrentPlanFactorial;
import clientservice.factory.UpdateEncouragementFactorial;

public class PlanDetailFragment extends Fragment implements IUpdatePartnerAsyncResponse {
    private CircleProgressBar mCustomProgressBar;
    private Button btnUpdateEncourage;
    private String realAmountMsg;
    private FragmentActivity myContext;
    private String uid;
    private SetEncouragementDialogFragement setEncouragementDialogFragement;

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
        Log.d("QuitSmokeDebug", "uid from previous view:" + uid);
        // set progress bar
        mCustomProgressBar = (CircleProgressBar)view.findViewById(R.id.custom_plan_detail_progress);
        // get current proceeding plan
        GetCurrentPlanFactorial getCurrentPlanFactorial = new GetCurrentPlanFactorial(getActivity(), uid, mCustomProgressBar, false);
        getCurrentPlanFactorial.delegate = this;
        getCurrentPlanFactorial.execute();

        // get panic button
        btnUpdateEncourage = (Button)view.findViewById(R.id.btn_update_encouragement);
        btnUpdateEncourage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("smokerUid", uid);
                setEncouragementDialogFragement = new SetEncouragementDialogFragement();
                setEncouragementDialogFragement.setArguments(bundle);
                setEncouragementDialogFragement.show(myContext.getSupportFragmentManager(), "updateEncouragement");
            }
        });
    }

    @Override
    public void processFinish(String reponseResult) {
        realAmountMsg = reponseResult;
    }
}
