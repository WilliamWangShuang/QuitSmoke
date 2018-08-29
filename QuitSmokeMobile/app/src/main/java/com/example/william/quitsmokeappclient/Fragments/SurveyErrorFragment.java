package com.example.william.quitsmokeappclient.Fragments;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.william.quitsmokeappclient.R;

public class SurveyErrorFragment extends DialogFragment {
    private boolean isSmokeValid;
    private boolean isAgeValid;
    private boolean isGenderValid;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        isSmokeValid = getArguments().getBoolean("isSmokeValid");
        isAgeValid = getArguments().getBoolean("isAgeValid");
        isGenderValid = getArguments().getBoolean("isGenderValid");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.survey_alert_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SurveyErrorFragment.this.getDialog().cancel();
                    }
                });

        // get textview in dialog
        TextView tvIsSmokeValid = (TextView)view.findViewById(R.id.tv_survey_dialog_smoke);
        TextView tvIsAgeValid = (TextView)view.findViewById(R.id.tv_survey_dialog_age);
        TextView tvIsGenderValid = (TextView)view.findViewById(R.id.tv_survey_dialog_gender);

        tvIsSmokeValid.setText(isSmokeValid ? "" : getActivity().getResources().getString(R.string.survey_smoke_err_msg));
        tvIsAgeValid.setText(isAgeValid ? "" : getActivity().getResources().getString(R.string.survey_age_err_msg));
        tvIsGenderValid.setText(isGenderValid ? "" : getActivity().getResources().getString(R.string.survey_gender_err_msg));

        return builder.create();
    }
}
