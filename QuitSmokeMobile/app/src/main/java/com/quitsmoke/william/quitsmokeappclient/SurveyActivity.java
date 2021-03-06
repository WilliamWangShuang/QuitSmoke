package com.quitsmoke.william.quitsmokeappclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quitsmoke.william.quitsmokeappclient.Fragments.SurveyErrorFragment;
import com.quitsmoke.william.quitsmokeappclient.Interface.IApprovePlanAsyncResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import clientservice.QuitSmokeClientUtils;
import clientservice.factory.LoginFactorial;
import clientservice.webservice.receiver.SyncNoSmokePlaceReceiver;

public class SurveyActivity extends AppCompatActivity implements IApprovePlanAsyncResponse {
    private EditText txtSmokePerDay;
    private Spinner ddlAge;
    private Spinner ddlGender;
    private Button btnNext;
    private TextView tvLaunch;
    private boolean isSmokePerDayValid;
    private boolean isGenderValid;
    private boolean isAgeValid;
    private Activity currActivity;
    private boolean isLoginSucc;
    private LoginFactorial loginFactorial;
    private SyncNoSmokePlaceReceiver syncNoSmokePlaceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gather_survey_info);
        currActivity = this;
        // get fields on page
        txtSmokePerDay = (EditText)findViewById((R.id.survey_smoke_per_day));
        ddlAge = (Spinner)findViewById((R.id.survey_age));
        ddlGender = (Spinner)findViewById((R.id.survey_gender));
        btnNext = (Button)findViewById(R.id.btn_servey_next);
        tvLaunch = findViewById(R.id.main_page_login);

        // set age spinner
        // Get reference of widgets from XML layout
        // initial spinner values
        List<String> valuesBuilder = new ArrayList<>();
        valuesBuilder.add(getResources().getString(R.string.spinner_age_selector));
        for (int i = 20; i <= 79; i++ ) {
            valuesBuilder.add("" + i);
        }
        ArrayAdapter<String> spinnerAgeAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, valuesBuilder.toArray(new String[0])
        );
        // Initializing an ArrayAdapter
        spinnerAgeAdapter.setDropDownViewResource(R.layout.spinner_item);
        ddlAge.setAdapter(spinnerAgeAdapter);

        // set gender spinner
        // initial spinner values
        List<String> genderValuesBuilder = new ArrayList<>();
        genderValuesBuilder.add(getResources().getString(R.string.spinner_gender_selector));
        genderValuesBuilder.add("M");
        genderValuesBuilder.add("F");
        ArrayAdapter<String> spinnerGenderAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, genderValuesBuilder.toArray(new String[0])
        );
        // Initializing an ArrayAdapter
        spinnerGenderAdapter.setDropDownViewResource(R.layout.spinner_item);
        ddlGender.setAdapter(spinnerGenderAdapter);

        // check if network connected, if yes, enable buttons
        if (haveNetworkConnection()) {
            tvLaunch.setEnabled(true);
            btnNext.setEnabled(true);

            tvLaunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get email and pwd from shared preference. If exist, direct sign in. Otherwise, go to login page
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    String emailInPreference = sharedPreferences.getString("email", "");
                    String pwdInPreference = sharedPreferences.getString("pwd", "");
                    if (emailInPreference == null
                            || "".equals(emailInPreference)
                            || pwdInPreference == null
                            || "".equals(pwdInPreference)) {
                        // go to register activity
                        Intent intent = new Intent(SurveyActivity.this, LaunchActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        LoginFactorial loginFactorial = new LoginFactorial(currActivity, emailInPreference, pwdInPreference, true);
                        loginFactorial.delegate = SurveyActivity.this;
                        loginFactorial.execute();
                    }
                }
            });

            // sync DB with SQLite only once each time app launch. After launch, it should not do this sync again when go back to this activity
            boolean isSmokeFreeZoneSync = QuitSmokeClientUtils.isIsSmokeFreeZoneSync();
            // start load no smoke place receiver
            if (!isSmokeFreeZoneSync) {
                Log.d("QuitSmokeDebug", "Start Sychronize Smoke Free Zone data between SQLite and Firebase.");
                syncNoSmokePlaceReceiver = new SyncNoSmokePlaceReceiver(this);
                QuitSmokeClientUtils.setIsSmokeFreeZoneSync(true);
            }

            // set onclick on button 'next'
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get user input value
                    String str_smoke_per_day = txtSmokePerDay.getText().toString();
                    String gender = ddlGender.getSelectedItem().toString();
                    String str_age = ddlAge.getSelectedItem().toString();
                    Intent intent = new Intent(SurveyActivity.this, SurveyResultActivity.class);
                    // if UI validate true, go to result activity, else show error message dialog
                    if (validateUI(str_smoke_per_day, gender, str_age)) {
                        int smoke_per_day = Integer.parseInt(str_smoke_per_day);
                        int age = Integer.parseInt(str_age);
                        intent.putExtra(v.getResources().getString(R.string.survey_smoke), smoke_per_day);
                        intent.putExtra(v.getResources().getString(R.string.survery_age), age);
                        intent.putExtra(v.getResources().getString(R.string.survey_gender), gender);

                        // start next seal page
                        startActivityForResult(intent, 1);
                    } else {
                        SurveyErrorFragment newFragment = new SurveyErrorFragment();
                        Bundle bundle = new Bundle();
                        // pass validation result to dialog
                        bundle.putBoolean("isAgeValid", isAgeValid);
                        bundle.putBoolean("isSmokeValid", isSmokePerDayValid);
                        bundle.putBoolean("isGenderValid", isGenderValid);
                        newFragment.setArguments(bundle);
                        newFragment.show(getSupportFragmentManager(), "missiles");
                    }
                }
            });
        } else {
            tvLaunch.setEnabled(false);
            btnNext.setEnabled(false);
            Toast.makeText(this, getResources().getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String emailInPreference = sharedPreferences.getString("email", "");
        String pwdInPreference = sharedPreferences.getString("pwd", "");
        // if back button pressed, go back to this launch page
        Intent intent = new Intent(SurveyActivity.this, SurveyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean validateUI(String smoke_per_day, String gender, String age) {
        boolean isValid = false;
        // check smoke per day
        try {
            Integer.parseInt(smoke_per_day);
            isSmokePerDayValid =true;
        } catch (NumberFormatException ex) {
            isSmokePerDayValid = false;
        }
        // check gender
        isGenderValid = "M".equals(gender) || "F".equals(gender);
        // check age
        try {
            Integer.parseInt(age);
            isAgeValid =true;
        } catch (NumberFormatException ex) {
            isAgeValid = false;
        }

        isValid = isSmokePerDayValid && isGenderValid && isAgeValid;

        return isValid;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void processFinish(boolean reponseResult) {
        isLoginSucc = reponseResult;
    }
}
