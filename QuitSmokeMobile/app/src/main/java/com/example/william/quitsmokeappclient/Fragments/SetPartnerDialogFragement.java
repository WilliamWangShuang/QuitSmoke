package com.example.william.quitsmokeappclient.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;

import ClientService.Entities.UpdatePartnerEntity;
import ClientService.Factory.UpdatePartnerFactorial;
import ClientService.QuitSmokeClientUtils;
import ClientService.webservice.QuitSmokeUserWebservice;

public class SetPartnerDialogFragement extends DialogFragment implements IUpdatePartnerAsyncResponse {
    private TextView tvErrorMsg;
    private EditText txtSetPartner;
    private UpdatePartnerFactorial updatePartnerFactorial;
    private String errorMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_partner_dialog, null);

        // get text view in dialog
        txtSetPartner = (EditText)view.findViewById(R.id.txtSetSupporter);
        //tvErrorMsg = (TextView)getParentFragment().getView().findViewById(R.id.tvMessage);
        tvErrorMsg = (TextView)getActivity().findViewById(R.id.tvMessage);

        // Inflate and set the layout for the dialog
        updatePartnerFactorial = new UpdatePartnerFactorial(txtSetPartner, getParentFragment(), getActivity());
        updatePartnerFactorial.delegate = this;
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updatePartnerFactorial.execute();
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SetPartnerDialogFragement.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void processFinish(String reponseResult) {
        errorMessage = reponseResult;
        tvErrorMsg.setText(errorMessage);
    }
}
