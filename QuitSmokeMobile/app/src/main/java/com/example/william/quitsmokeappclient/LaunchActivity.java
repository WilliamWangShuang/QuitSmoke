package com.example.william.quitsmokeappclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import clientservice.factory.LoginFactorial;

public class LaunchActivity extends AppCompatActivity {

    // declare text field email & password
    private EditText txtEmail;
    private EditText txtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_launcher);

        // get text field email & password
        txtEmail = (EditText)findViewById(R.id.emailEditText);
        txtPwd = (EditText)findViewById(R.id.passwordEditText);

        // sign in button click
        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get value in text field email & password
                String emailFromUI = txtEmail.getText().toString();
                String pwdFromUI = txtPwd.getText().toString();
                // check if the user exist by email and password
                LoginFactorial loginFactorial = new LoginFactorial(LaunchActivity.this, emailFromUI, pwdFromUI);

                loginFactorial.execute();
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
}
