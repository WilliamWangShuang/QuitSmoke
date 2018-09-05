package com.example.william.quitsmokeappclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.Fragments.SurveyErrorFragment;
import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {
    private EditText txtSmokePerDay;
    private Spinner ddlAge;
    private Spinner ddlGender;
    private Button btnNext;
    private boolean isSmokePerDayValid;
    private boolean isGenderValid;
    private boolean isAgeValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gather_survey_info);

        // get fields on page
        txtSmokePerDay = (EditText)findViewById((R.id.survey_smoke_per_day));
        ddlAge = (Spinner)findViewById((R.id.survey_age));
        ddlGender = (Spinner)findViewById((R.id.survey_gender));
        btnNext = (Button)findViewById(R.id.btn_servey_next);

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

        TextView tvLaunch = findViewById(R.id.main_page_login);
        tvLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to register activity
                Intent intent = new Intent(SurveyActivity.this, LaunchActivity.class);
                startActivityForResult(intent, 1);
            }
        });

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
}
