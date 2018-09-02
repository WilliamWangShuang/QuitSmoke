package com.example.william.quitsmokeappclient.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.R;

public class CreatePlanErrorFragement extends DialogFragment {
    private boolean isTargetNoValid;
    private boolean isPartberSet;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        isTargetNoValid = getArguments().getBoolean("isTargetNoValid");
        isPartberSet = getArguments().getBoolean("isPartberSet");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_plan_error_message, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreatePlanErrorFragement.this.getDialog().cancel();
                    }
                });

        // get text view in dialog
        TextView tvIsTargetNoValid = (TextView)view.findViewById(R.id.tv_create_plan_empty_target);
        TextView tvIsPartnerSet = (TextView)view.findViewById((R.id.tv_create_plan_no_partner));

        tvIsTargetNoValid.setText(isTargetNoValid ? "" : getActivity().getResources().getString(R.string.create_plan_empty_target));
        tvIsPartnerSet.setText(isPartberSet ? "" : getActivity().getResources().getString(R.string.create_plan_no_partner));

        return builder.create();
    }
}
