package com.example.william.quitsmokeappclient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ClientService.Entities.UserInfoEntity;
import ClientService.Factory.RegisterFactorial;
import ClientService.QuitSmokeClientUtils;

public class RegisterActivity extends AppCompatActivity {

    // declare error message labels
    private TextView msgName;
    private TextView msgEmail;
    private TextView msgPwd;
    private TextView msgCity;
    private TextView msgSuburb;
    private TextView msgAge;
    private TextView msgGender;
    // declare text fields
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtCity;
    private EditText txtSuburb;
    private TextView txtPwd;
    // declare drop down lists
    private Spinner ddlRole;
    private Spinner ddlAge;
    private Spinner ddlGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // set tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get drop down lists
        ddlRole = (Spinner)findViewById(R.id.ddlRole);
        ddlAge = (Spinner)findViewById(R.id.ddlAge);
        ddlGender = (Spinner)findViewById(R.id.ddlGender);

        // set role spinner
        // Get reference of widgets from XML layout
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, getResources().getStringArray(R.array.user_role)
        );
        // Initializing an ArrayAdapter
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        ddlRole.setAdapter(spinnerArrayAdapter);

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

        // get error messages labels
        msgName = (TextView)findViewById(R.id.lblNameNoErrorMsg);
        msgEmail = (TextView)findViewById(R.id.lblEmailErrorMsg);
        msgPwd = (TextView)findViewById(R.id.lblPwdErrorMsg);
        msgCity = (TextView)findViewById(R.id.lblCityErrorMsg);
        msgSuburb = (TextView)findViewById(R.id.lblSuburbErrorMsg);
        msgAge = (TextView)findViewById(R.id.lblAgeErrorMsg);
        msgGender = (TextView)findViewById(R.id.lblGenderErrorMsg);
        // get text fields
        txtName = (EditText)findViewById(R.id.register_name);
        txtEmail = (EditText)findViewById(R.id.register_email);
        txtPwd = (TextView)findViewById(R.id.register_password);
        txtCity = (EditText)findViewById(R.id.register_city);
        txtSuburb = (EditText)findViewById(R.id.register_suburb);

        // register button logic
        Button btnRegister = (Button)findViewById(R.id.btn_confirm_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get input fields values
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String pwd = txtPwd.getText().toString();
                String city = txtCity.getText().toString();
                String suburb = txtSuburb.getText().toString();
                String age = ddlAge.getSelectedItem().toString();
                String gender = ddlGender.getSelectedItem().toString();
                boolean isSmoker = ddlRole.isSelected()
                        && getResources().getString(R.string.role_smoker).equals((String)ddlRole.getSelectedItem());
                boolean isSupporter = ddlRole.isSelected()
                        && getResources().getString(R.string.role_supporter).equals((String)ddlRole.getSelectedItem());

                // UI validation
                // create UI info entity object
                UserInfoEntity registerInfoUI = new UserInfoEntity(name, age, gender, email, pwd, city, suburb, isSmoker, isSupporter);
                // validate fields on UI
                boolean isDataValidate = validateUIFields(registerInfoUI);
                // if UI validation pass, do server-side validation
                if (isDataValidate) {
                    RegisterFactorial registerFactorial = new RegisterFactorial(RegisterActivity.this, txtEmail, registerInfoUI);
                    registerFactorial.execute();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // go to home activity
            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // validate UI fields
    private boolean validateUIFields(UserInfoEntity entity) {
        boolean result = false;
        // validate first name
        result = QuitSmokeClientUtils.validateEmpty(entity.getName(), getResources().getString(R.string.register_name_empty_msg), msgName);
        // validate city
        result = QuitSmokeClientUtils.validateEmpty(entity.getCity(), getResources().getString(R.string.register_city_empty_msg), msgCity) && result;
        // validate suburb
        result = QuitSmokeClientUtils.validateEmpty(entity.getSuburb(), getResources().getString(R.string.register_suburb_empty_msg), msgSuburb) && result;
        // validate age
        result = QuitSmokeClientUtils.validateAge(entity.getAge(), getResources().getString(R.string.register_age), msgAge) && result;
        // validate gender
        result = QuitSmokeClientUtils.validateGender(entity.getGender(), getResources().getString(R.string.register_gender), msgGender) && result;
        // validate email
        result = QuitSmokeClientUtils.validateEmpty(entity.getEmail(), getResources().getString(R.string.register_email_empty_msg), msgEmail) && result;
        result = QuitSmokeClientUtils. validateEmailFormat(entity.getEmail(), getResources().getString(R.string.register_email_format_msg), msgEmail) && result;
        // validate password
        result = QuitSmokeClientUtils.validateEmpty(entity.getPassword(), getResources().getString(R.string.register_pwd_empty_msg), msgPwd) && result;
        result = QuitSmokeClientUtils.validatePwdFormat(entity.getPassword(), getResources().getString(R.string.register_pwd_format_msg), msgPwd) && result;
        return result;
    }
}
