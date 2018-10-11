package com.quitsmoke.william.quitsmokeappclient.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quitsmoke.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.quitsmoke.william.quitsmokeappclient.R;

import clientservice.QuitSmokeClientConstant;
import clientservice.factory.UpdateEncouragementFactorial;
import clientservice.factory.UpdateMilestoneFactorial;

public class SetMilestoneDialogFragement extends DialogFragment implements IUpdatePartnerAsyncResponse {
    private EditText txtSetMilestoneTarget;
    private EditText txtSetReward;
    private TextView tvTargetMilstone;
    private String uid;
    private int currentSuccessiveDays;
    private int targetNo;
    private TextView tvMilestone;
    private UpdateMilestoneFactorial updateMilestoneFactorial;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_milestone_dialog, null);

        // get edit view in dialog
        txtSetMilestoneTarget = (EditText)view.findViewById(R.id.txtSetMilestoneTarget);
        txtSetReward = (EditText)view.findViewById(R.id.txtSetReward);
        tvMilestone = (TextView)getActivity().findViewById(R.id.tv_plan_detail_milestone);
        tvTargetMilstone = (TextView)view.findViewById(R.id.tvMilstoneTargetMsg);

        // initial update milestone factory
        uid = getArguments().getString("smokerUid");
        currentSuccessiveDays = getArguments().getInt("currentPoint");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        tvTargetMilstone.setText("");
                        SetMilestoneDialogFragement.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("TestDebug", "tvTargetMilstone:" + tvTargetMilstone.getText().toString());
        if (!"".equals(tvTargetMilstone.getText().toString())) {

        }
    }

    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = false;
                    // initial update milestone factory
                    String targetStr = txtSetMilestoneTarget.getText().toString();
                    boolean isInputEmpty = targetStr == null || "".equals(targetStr);
                    if (!isInputEmpty) {
                        targetNo = Integer.parseInt(targetStr);
                        tvTargetMilstone.setText("");
                        String reward = txtSetReward.getText().toString();
                        updateMilestoneFactorial = new UpdateMilestoneFactorial(uid, targetNo, reward);
                        updateMilestoneFactorial.delegate = SetMilestoneDialogFragement.this;
                        // call REST method to update milestone of the plan
                        updateMilestoneFactorial.execute();
                        wantToCloseDialog = true;
                    } else {
                        tvTargetMilstone.setText(getResources().getString(R.string.milestone_target_error));
                    }
                    if(wantToCloseDialog)
                        dismiss();
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }

    @Override
    public void processFinish(String reponseResult) {
        if (QuitSmokeClientConstant.INDICATOR_Y.equals(reponseResult)) {
            // construct milestone text
            String milestone = (currentSuccessiveDays >= targetNo ? "Complete\n" : "") + currentSuccessiveDays + "/" + targetNo;
            tvMilestone.setText(milestone);
        }
    }
}
