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

public class MessageDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.message_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MessageDialogFragment.this.getDialog().cancel();
                    }
                });

        // get textview in dialog
        TextView tvMessage = (TextView)view.findViewById(R.id.tv_message);
        // get message value from bundle
        String message = getArguments().getString("message");
        tvMessage.setText(message);

        return builder.create();
    }
}
