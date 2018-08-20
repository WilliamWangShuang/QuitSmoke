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
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
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
    // declare text fields
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtCity;
    private EditText txtSuburb;
    private TextView txtPwd;
    // declare check box
    private Spinner ddlRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // set tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get error messages labels
        msgName = (TextView)findViewById(R.id.lblNameNoErrorMsg);
        msgEmail = (TextView)findViewById(R.id.lblEmailErrorMsg);
        msgPwd = (TextView)findViewById(R.id.lblPwdErrorMsg);
        msgCity = (TextView)findViewById(R.id.lblCityErrorMsg);
        msgSuburb = (TextView)findViewById(R.id.lblSuburbErrorMsg);
        // get text fields
        txtName = (EditText)findViewById(R.id.register_name);
        txtEmail = (EditText)findViewById(R.id.register_email);
        txtPwd = (TextView)findViewById(R.id.register_password);
        txtCity = (EditText)findViewById(R.id.register_city);
        txtSuburb = (EditText)findViewById(R.id.register_suburb);
        // get checkbox
        ddlRole = (Spinner)findViewById(R.id.ddlRole);

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
                boolean isSmoker = ddlRole.isSelected()
                        && getResources().getString(R.string.role_smoker).equals((String)ddlRole.getSelectedItem());

                // UI validation
                // create UI info entity object
                UserInfoEntity registerInfoUI = new UserInfoEntity(name, email, pwd, city, suburb, isSmoker);
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

    // Handles check box event
    /*public void onCheckboxClicked(View view){
        boolean smokerChecked = chkSmoker.isChecked();
        boolean supporterChecked = chkSupporter.isChecked();

        if (smokerChecked && supporterChecked)
        {
            // if user is both smoker and supporter
        }
        else if (smokerChecked){
            // if user is a smoker
        }
        else {
            // if user is a supporter
        }

    }*/

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
        result = QuitSmokeClientUtils.validateEmpty(entity.getCity(), getResources().getString(R.string.register_city_empty_msg), msgCity);
        // validate suburb
        result = QuitSmokeClientUtils.validateEmpty(entity.getSuburb(), getResources().getString(R.string.register_suburb_empty_msg), msgSuburb);
        // validate email
        result = QuitSmokeClientUtils.validateEmpty(entity.getEmail(), getResources().getString(R.string.register_email_empty_msg), msgEmail) && result;
        result = QuitSmokeClientUtils. validateEmailFormat(entity.getEmail(), getResources().getString(R.string.register_email_format_msg), msgEmail) && result;
        // validate password
        result = QuitSmokeClientUtils.validateEmpty(entity.getPassword(), getResources().getString(R.string.register_pwd_empty_msg), msgPwd) && result;
        result = QuitSmokeClientUtils.validatePwdFormat(entity.getPassword(), getResources().getString(R.string.register_pwd_format_msg), msgPwd) && result;
        return result;
    }
}
