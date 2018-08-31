package com.example.william.quitsmokeappclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.Interface.ISurveyResultAsyncResponse;
import ClientService.Entities.SurveyResultEntity;
import ClientService.Factory.SurveyResultFactorial;

public class SurveyResultActivity extends AppCompatActivity implements ISurveyResultAsyncResponse {
    private int age;
    private int smoke_per_day;
    private String gender;
    private TextView tvMeanSmoke;
    private TextView tvChanceQuitting;
    private Button btnStart;
    private SurveyResultEntity resultFromFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_result);

        // get text views on page
        tvMeanSmoke = (TextView)findViewById(R.id.tvMeanSmoke);
        tvChanceQuitting = (TextView)findViewById((R.id.tvChanceQuitting));
        btnStart = (Button)findViewById(R.id.btn_servey_result_next);

        Bundle bundle = this.getIntent().getExtras();
        // get age
        String ageIntentKey = getResources().getString(R.string.survery_age);
        age = bundle.getInt(ageIntentKey);
        // get smoke per day
        String smokePerDayIntentKey = getResources().getString(R.string.survey_smoke);
        smoke_per_day = bundle.getInt(smokePerDayIntentKey);
        // get gender
        String genderIntentKey = getResources().getString(R.string.survey_gender);
        gender = bundle.getString(genderIntentKey);

        int mean = smoke_per_day * 7;
        // call ws to get survey relevant data from backend
        SurveyResultFactorial surveyResultFactorial = new SurveyResultFactorial(this, age, gender, mean);
        //this to set delegate/listener back to this class
        surveyResultFactorial.delegate = this;
        surveyResultFactorial.execute();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyResultActivity.this, TopMotivationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable ("responsResult", resultFromFactory);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void processFinish(SurveyResultEntity reponseResult) {
        resultFromFactory = reponseResult;
    }
}
