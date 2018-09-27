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

import clientservice.QuitSmokeClientUtils;

public class ViewMilestoneInfoDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_milestone_info_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ViewMilestoneInfoDialogFragment.this.getDialog().cancel();
                    }
                });

        // get textview in dialog
        TextView tvMessage = (TextView)view.findViewById(R.id.tv_message);
        TextView tvReward = (TextView)view.findViewById(R.id.tv_reward);
        // get message value from bundle
        int message = getArguments().getInt("message");
        tvMessage.setText("You have insist " + message + " days.");
        tvReward.setText("Your partner promise: " + QuitSmokeClientUtils.getReward());

        return builder.create();
    }
}
