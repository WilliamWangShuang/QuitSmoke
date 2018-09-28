package com.example.william.quitsmokeappclient.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;
import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.factory.GetCurrentPlanFactorial;

public class SmokerMainFragment extends Fragment implements IUpdatePartnerAsyncResponse {
    private CircleProgressBar mCustomProgressBar;
    private Button btnPanicButton;
    private String realAmountMsg;
    private CreatePlanErrorFragement createPlanErrorFragment;
    private GoToEncourageDialogFragment goToEncourageDialogFragment;
    private ViewMilestoneInfoDialogFragment viewMilestoneInfoDialogFragment;
    private TextView tvMilestone;
    private TextView tvMoneySaved;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.smoker_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createPlanErrorFragment = new CreatePlanErrorFragement();
        viewMilestoneInfoDialogFragment = new ViewMilestoneInfoDialogFragment();
        // textview for milestone
        tvMilestone = (TextView)view.findViewById(R.id.tv_milestone_progress);
        // textview for money saved
        tvMoneySaved = (TextView)view.findViewById(R.id.tv_money_saved);
        // set progress bar
        mCustomProgressBar = (CircleProgressBar)view.findViewById(R.id.custom_progress);
        // get current proceeding plan
        GetCurrentPlanFactorial getCurrentPlanFactorial = new GetCurrentPlanFactorial(getActivity(), QuitSmokeClientUtils.getUid(), mCustomProgressBar, tvMilestone, tvMoneySaved, true);
        getCurrentPlanFactorial.delegate = this;
        getCurrentPlanFactorial.execute();

        // set panic button
        btnPanicButton = (Button)view.findViewById(R.id.buttonforquitter);
        btnPanicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QuitSmokeDebug", "Result from GetCurrentPlanFactorial:" + realAmountMsg);
                // if user not set partner, give error message
                if (realAmountMsg != null && !QuitSmokeClientConstant.INDICATOR_N.equals(realAmountMsg) && !QuitSmokeClientConstant.INDICATOR_NO_PLAN.equals(realAmountMsg)) {
                    // get real amount message
                    String msg = String.format(getResources().getString(R.string.panic_button_msg), realAmountMsg);
                    // initial pop out dialogs on this view
                    goToEncourageDialogFragment = new GoToEncourageDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("message", msg);
                    goToEncourageDialogFragment.setArguments(args);
                    goToEncourageDialogFragment.show(getFragmentManager(), "go to encouragement");
                } else if (QuitSmokeClientConstant.INDICATOR_NO_PLAN.equals(realAmountMsg)) {
                    mCustomProgressBar.setVisibility(View.INVISIBLE);
                    // get validation value to bundle to pass to error dialog entity
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isTargetNoValid", true);
                    bundle.putBoolean("isPartberSet", true);
                    bundle.putBoolean("isPlanCreated", false);
                    bundle.putBoolean("isProceedingPlanExist", false);
                    createPlanErrorFragment.setArguments(bundle);
                    createPlanErrorFragment.show(getFragmentManager(), "no running plan");
                } else {
                    // get validation value to bundle to pass to error dialog entity
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isTargetNoValid", true);
                    bundle.putBoolean("isPartberSet", true);
                    bundle.putBoolean("isPlanCreated", false);
                    bundle.putBoolean("isProceedingPlanExist", false);
                    createPlanErrorFragment.setArguments(bundle);
                    createPlanErrorFragment.show(getFragmentManager(), "no partner");
                }
            }
        });

        // set onclick event for textview Milestone
        tvMilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("message", QuitSmokeClientUtils.getPoint());
                viewMilestoneInfoDialogFragment.setArguments(bundle);
                viewMilestoneInfoDialogFragment.show(getFragmentManager(), "see promise");
            }
        });
    }


    @Override
    public void processFinish(String reponseResult) {
        realAmountMsg = reponseResult;
        tvMilestone.setEnabled(true);
        btnPanicButton.setEnabled(true);
    }
}
