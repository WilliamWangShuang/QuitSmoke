package com.example.william.quitsmokeappclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import clientservice.factory.CreateSupporterFactorial;

public class CreateSupporterActivity extends AppCompatActivity {
    private EditText txtSupporterEmail;
    private Button btnSave;
    private Button btnLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.designate_supporter);

        // get views from UI
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
                intent.putExtra("isFromRegister", true);
                startActivityForResult(intent, 1);
            }
        });
    }
}
