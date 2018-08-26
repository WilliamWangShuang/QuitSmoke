package com.example.william.quitsmokeappclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ClientService.Factory.CreateSupporterFactorial;

public class CreateSupporterActivity extends AppCompatActivity {
    private TextView tvSupporterEmailErrorMsg;
    private EditText txtSupporterEmail;
    private Button btnSave;
    private Button btnLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.designate_supporter);

        // get views from UI
        tvSupporterEmailErrorMsg = (TextView)findViewById(R.id.lblAddSupporterMsg);
        txtSupporterEmail = (EditText)findViewById(R.id.txtAddSupporter);
        btnSave = (Button)findViewById(R.id.btnAddSupporter);
        btnLater = (Button)findViewById(R.id.btnAddLater);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supporterEmail = txtSupporterEmail.getText().toString();
                // check if the user exist by email and password
                CreateSupporterFactorial createSupporterFactorial = new CreateSupporterFactorial(CreateSupporterActivity.this, supporterEmail);

                createSupporterFactorial.execute();
            }
        });

        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSupporterActivity.this, MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}
