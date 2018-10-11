package com.quitsmoke.william.quitsmokeappclient.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.quitsmoke.william.quitsmokeappclient.R;

public class CreatePlanErrorFragement extends DialogFragment {
    private boolean isTargetNoValid;
    private boolean isPartberSet;
    private boolean isProceedingPlanExist;
    private boolean isPlanCreated;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        isTargetNoValid = getArguments().getBoolean("isTargetNoValid");
        isPartberSet = getArguments().getBoolean("isPartberSet");
        isProceedingPlanExist = getArguments().getBoolean("isProceedingPlanExist");
        isPlanCreated = getArguments().getBoolean("isPlanCreated");

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
        TextView tvIsPartnerSet = (TextView)view.findViewById(R.id.tv_create_plan_no_partner);
        TextView tvProceedingPlan = view.findViewById(R.id.tv_create_plan_proceeding_plan_exist);
        TextView tvNoPlanCreate = view.findViewById(R.id.tv_not_create_plan);

        tvIsTargetNoValid.setText(isTargetNoValid ? "" : getActivity().getResources().getString(R.string.create_plan_empty_target));
        tvIsPartnerSet.setText(isPartberSet ? "" : getActivity().getResources().getString(R.string.create_plan_no_partner));
        tvProceedingPlan.setText(isProceedingPlanExist ? getActivity().getResources().getString(R.string.create_plan_proceeding_task_exist) : "");
        tvNoPlanCreate.setText(isPlanCreated ? "" : getActivity().getResources().getString(R.string.not_create_plan_msg));

        return builder.create();
    }
}
