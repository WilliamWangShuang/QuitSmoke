package com.quitsmoke.william.quitsmokeappclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quitsmoke.william.quitsmokeappclient.Interface.IApprovePlanAsyncResponse;

import clientservice.factory.CircularProgressButton;
import clientservice.factory.LoginFactorial;

public class LaunchActivity extends AppCompatActivity implements IApprovePlanAsyncResponse {

    // declare text field email & password
    private EditText txtEmail;
    private EditText txtPwd;
    private CircularProgressButton btnMorph1;
    private int mMorphCounter1;
    private boolean isLoginSucc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("QuitSmokeDebug", "LaunchActivity onCreate Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_launcher);

        // get text field email & password
        txtEmail = (EditText)findViewById(R.id.emailEditText);
        txtPwd = (EditText)findViewById(R.id.passwordEditText);

        // sign in button click
        mMorphCounter1 = 1;
        btnMorph1 = (CircularProgressButton)findViewById(R.id.btnMorph1);
        btnMorph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton1Clicked(btnMorph1);
            }
        });

        TextView registerTextView = findViewById(R.id.registerLink);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to register activity
                Intent intent = new Intent(LaunchActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void morphToSquare(final CircularProgressButton btnMorph, int duration) {
        CircularProgressButton.Params square = CircularProgressButton.Params.create()
                .duration(duration)
                .cornerRadius(R.dimen.mb_corner_radius_2)
                .width((int)getResources().getDimension(R.dimen.mb_width_200))
                .height((int)getResources().getDimension(R.dimen.mb_height_56))
                .color(getResources().getColor(R.color.mb_blue))
                .colorPressed(getResources().getColor(R.color.mb_blue_dark))
                .text(getResources().getString(R.string.login));
        btnMorph.morph(square);
    }

    private void morphToFailure(final CircularProgressButton btnMorph) {
        CircularProgressButton.Params circle = CircularProgressButton.Params.create()
                .duration(500)
                .cornerRadius(R.dimen.mb_corner_radius_2)
                .width((int)getResources().getDimension(R.dimen.mb_height_56))
                .height((int)getResources().getDimension(R.dimen.mb_height_56))
                .color(getResources().getColor(R.color.holo_red_light))
                .colorPressed(getResources().getColor(R.color.holo_red_dark))
                .icon(R.drawable.ic_lock);
        btnMorph.morph(circle);
    }

    private void morphToSuccess(final CircularProgressButton btnMorph) {
        CircularProgressButton.Params circle = CircularProgressButton.Params.create()
                .duration(500)
                .cornerRadius(R.dimen.mb_corner_radius_2)
                .width((int)getResources().getDimension(R.dimen.mb_height_56))
                .height((int)getResources().getDimension(R.dimen.mb_height_56))
                .color(getResources().getColor(R.color.holo_green_light))
                .colorPressed(getResources().getColor(R.color.holo_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }

    private void onMorphButton1Clicked(final CircularProgressButton btnMorph) {
        if (mMorphCounter1 == 0) {
                mMorphCounter1++;
                morphToSquare(btnMorph, 500);
        } else if (mMorphCounter1 == 1) {
            String emailFromUI = txtEmail.getText().toString();
            String pwdFromUI = txtPwd.getText().toString();
            // check if the user exist by email and password
            LoginFactorial loginFactorial = new LoginFactorial(LaunchActivity.this, emailFromUI, pwdFromUI, false);
            loginFactorial.delegate = this;
            loginFactorial.execute();
        }
    }

    @Override
    public void processFinish(boolean reponseResult) {
        isLoginSucc = reponseResult;
        // not found this user, set login button to fail style
        mMorphCounter1 = 0;
        if (!isLoginSucc) {
            Log.d("QuitSmokeDebug","This user not found");
            morphToFailure(btnMorph1);
        } else {
            morphToSuccess(btnMorph1);
            btnMorph1.setClickable(false);
        }
    }
}
