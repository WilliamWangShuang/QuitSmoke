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

public class CalculateFRSErrorFragment extends DialogFragment {
    private boolean isCholValid;
    private boolean isSBPValid;
    private boolean isHDLValid;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        isCholValid = getArguments().getBoolean("isCholValid");
        isSBPValid = getArguments().getBoolean("isSBPValid");
        isHDLValid = getArguments().getBoolean("isHDLValid");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.calculate_alert_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CalculateFRSErrorFragment.this.getDialog().cancel();
                    }
                });

        // get textview in dialog
        TextView tvIsCholValid = (TextView)view.findViewById(R.id.tv_calculate_dialog_chol);
        TextView tvIsSBPValid = (TextView)view.findViewById(R.id.tv_calculate_dialog_sbp);
        TextView tvIsHDLValid = (TextView)view.findViewById(R.id.tv_calculate_dialog_hdl);

        tvIsCholValid.setText(isCholValid ? "" : getActivity().getResources().getString(R.string.calculate_chol_err_msg));
        tvIsSBPValid.setText(isSBPValid ? "" : getActivity().getResources().getString(R.string.calculate_sbp_err_msg));
        tvIsHDLValid.setText(isHDLValid ? "" : getActivity().getResources().getString(R.string.calculate_hdl_err_msg));

        return builder.create();
    }
}
