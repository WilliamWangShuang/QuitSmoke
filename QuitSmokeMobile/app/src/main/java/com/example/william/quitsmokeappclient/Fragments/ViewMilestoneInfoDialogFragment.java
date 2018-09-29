package com.example.william.quitsmokeappclient.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.william.quitsmokeappclient.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import clientservice.QuitSmokeClientUtils;

public class ViewMilestoneInfoDialogFragment extends DialogFragment {
    private Button btnFacebookShare;
    private SharePhotoContent content;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

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
        tvMessage.setText(String.format("You have insist %d days.", message));
        tvReward.setText(String.format("Your partner promise: %s", QuitSmokeClientUtils.getReward()));

        // Initialize the SDK before executing any other operations,
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(this.requireContext());

        // construct share content
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cup);
        // customized the picture that is to be shared
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        // draw the No. of quit days on bitmap
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(100);
        // calculate No. x coordinate
        float x = 0;
        if (message > 0 && message < 10)
            x = canvas.getWidth()/2 - 28;
        else if (message >=10 && message <= 99)
            x = canvas.getWidth()/2 - 56;
        else
            x = canvas.getWidth()/2 - 80;
        canvas.drawText(message + "", x,canvas.getHeight()/3 + 35,paint);
        // draw information message
        paint.setTextSize(40);
        canvas.drawText("I have quitted for",canvas.getWidth()/4 + 35,canvas.getHeight()/4 - 35,paint);
        canvas.drawText("Day(s)",canvas.getWidth()/2 - 56,canvas.getHeight()/2 + 35,paint);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(mutableBitmap)
                .build();
        content =  new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        // construct fb dialog
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // get fb share button
        btnFacebookShare = view.findViewById(R.id.btn_fb_share);
        // set facebook share button onclick event
        btnFacebookShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShareDialog.canShow(SharePhotoContent.class)) {
                    shareDialog.show(content);
                }
            }
        });

        return builder.create();
    }
}
