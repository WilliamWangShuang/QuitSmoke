package com.example.william.quitsmokeappclient.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.william.quitsmokeappclient.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ClientService.Entities.CalculateFrsEntity;
import ClientService.Factory.CalculateFrsFactorial;
import ClientService.QuitSmokeClientUtils;

public class CalculateFrsFragment extends Fragment {
    private View vCalculateFrsFragment;
    private EditText txtChol;
    private EditText txtSBP;
    private EditText txtHDL;
    private Button btnSubmit;
    private CalculateFrsEntity calculateFrsEntity;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vCalculateFrsFragment = inflater.inflate(R.layout.fragment_calculate_frs, container, false);

        return vCalculateFrsFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);

            // get fields from UI
            txtChol = (EditText) getActivity().findViewById(R.id.txtChol);
            txtSBP = (EditText) getActivity().findViewById(R.id.txtSBP);
            txtHDL = (EditText) getActivity().findViewById(R.id.txtHDL);
            btnSubmit = (Button) getActivity().findViewById(R.id.btnCalculateFrs);

            //registering btnSubmit with onclicklistener
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // get values from fields and application level constants for request
                    int chol = Integer.parseInt(txtChol.getText().toString());
                    int sbp = Integer.parseInt(txtSBP.getText().toString());
                    int hdl = Integer.parseInt(txtHDL.getText().toString());
                    int age = QuitSmokeClientUtils.getAge();
                    final String gender = QuitSmokeClientUtils.getGender();
                    final boolean isTreated = false; //TODO: should come from UI
                    // construct UI entity
                    calculateFrsEntity = new CalculateFrsEntity(age, gender, chol, hdl, sbp, isTreated);

                    CalculateFrsFactorial registerFactorial = new CalculateFrsFactorial(getActivity(), calculateFrsEntity);
                    registerFactorial.execute();
                }
            });
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }
    }
}
