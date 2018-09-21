package com.example.william.quitsmokeappclient.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;

import clientservice.QuitSmokeClientConstant;
import clientservice.factory.UpdateEncouragementFactorial;
import clientservice.factory.UpdatePartnerFactorial;

public class SetEncouragementDialogFragement extends DialogFragment implements IUpdatePartnerAsyncResponse {
    private EditText txtSetEncouragement;
    private UpdateEncouragementFactorial updateEncouragementFactorial;
    private TextView tvMessage;
    private String succMessage;
    private String errorMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_encouragement_dialog, null);

        // get text view in dialog
        txtSetEncouragement = (EditText)view.findViewById(R.id.txtSetEncouragement);
        tvMessage = getActivity().findViewById(R.id.tvMessage);

        // get successful and fail message string
        succMessage = getActivity().getResources().getString(R.string.succ_update_encourgement);
        errorMessage = getActivity().getResources().getString(R.string.error_msg);

        // Inflate and set the layout for the dialog
        Log.d("TestDebug", "1.newEncourage:" + txtSetEncouragement.getText().toString());
//        updateEncouragementFactorial = new UpdateEncouragementFactorial(getArguments().getString("smokerUid"), txtSetEncouragement.getText().toString(), getActivity());
//        updateEncouragementFactorial.delegate = this;
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateEncouragementFactorial = new UpdateEncouragementFactorial(getArguments().getString("smokerUid"), txtSetEncouragement.getText().toString(), getActivity());
                        updateEncouragementFactorial.delegate = SetEncouragementDialogFragement.this;
                        updateEncouragementFactorial.execute();
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SetEncouragementDialogFragement.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void processFinish(String reponseResult) {
        if (QuitSmokeClientConstant.INDICATOR_Y.equals(reponseResult))
            tvMessage.setText(succMessage);
        else
            tvMessage.setText(errorMessage);
    }
}
