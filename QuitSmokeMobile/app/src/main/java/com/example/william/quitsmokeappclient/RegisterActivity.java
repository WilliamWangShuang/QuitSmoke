package com.example.william.quitsmokeappclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import clientservice.entities.UserInfoEntity;
import clientservice.factory.RegisterFactorial;
import clientservice.QuitSmokeClientUtils;

public class RegisterActivity extends AppCompatActivity {

    // declare error message labels
    private TextView msgName;
    private TextView msgEmail;
    private TextView msgPwd;
    private TextView msgAge;
    private TextView msgGender;
    private TextView msgPricePerPack;
    // declare text fields
    private EditText txtName;
    private EditText txtEmail;
    private TextView txtPwd;
    private EditText txtPricePerPack;
    // declare drop down lists
    private Spinner ddlRole;
    private Spinner ddlAge;
    private Spinner ddlGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

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
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(ddlAge);

            // Set popupWindow height to 500px
            popupWindow.setHeight(800);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

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
        msgAge = (TextView)findViewById(R.id.lblAgeErrorMsg);
        msgGender = (TextView)findViewById(R.id.lblGenderErrorMsg);
        msgPricePerPack = (TextView)findViewById(R.id.lblPricePerPackErrorMsg);
        // get text fields
        txtName = (EditText)findViewById(R.id.register_name);
        txtEmail = (EditText)findViewById(R.id.register_email);
        txtPwd = (TextView)findViewById(R.id.register_password);
        txtPricePerPack = (EditText)findViewById(R.id.register_price_per_pack);

        // register button logic
        Button btnRegister = (Button)findViewById(R.id.btn_confirm_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get input fields values
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String pwd = txtPwd.getText().toString();
                String age = ddlAge.getSelectedItem().toString();
                String gender = ddlGender.getSelectedItem().toString();
                boolean isSmoker = getResources().getString(R.string.i_am_a_smoker).equals(ddlRole.getSelectedItem().toString());
                boolean isSupporter = getResources().getString(R.string.supporter).equals(ddlRole.getSelectedItem().toString());
                String pricePerPack = txtPricePerPack.getText().toString();

                // UI validation
                // create UI info entity object
                UserInfoEntity registerInfoUI = new UserInfoEntity(name, age, gender, email, pwd, isSmoker, isSupporter, pricePerPack);
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

    // validate UI fields
    private boolean validateUIFields(UserInfoEntity entity) {
        boolean result = false;
        // validate first name
        result = QuitSmokeClientUtils.validateEmpty(entity.getName(), getResources().getString(R.string.register_name_empty_msg), msgName);
        // validate price per pack
        result = QuitSmokeClientUtils.validatePricePerPack(entity.getPricePerPack(), getResources().getString(R.string.register_price_per_pack_msg), msgPricePerPack) && result;
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
