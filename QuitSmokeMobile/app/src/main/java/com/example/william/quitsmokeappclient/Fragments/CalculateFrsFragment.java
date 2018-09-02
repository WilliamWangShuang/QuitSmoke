package com.example.william.quitsmokeappclient.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
    private boolean isCholValid;
    private boolean isSBPValid;
    private boolean isHDLValid;
    private FragmentActivity myContext;
    private TextView tvHintChol;
    private TextView tvHintSBP;
    private TextView tvHintHDL;
    private MessageDialogFragment messageFragment;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

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
            txtChol = (EditText) view.findViewById(R.id.txtChol);
            txtSBP = (EditText) view.findViewById(R.id.txtSBP);
            txtHDL = (EditText) view.findViewById(R.id.txtHDL);
            btnSubmit = (Button) view.findViewById(R.id.btnCalculateFrs);
            tvHintChol = (TextView) view.findViewById(R.id.tvChol);
            tvHintSBP = (TextView) view.findViewById(R.id.tvSBP);
            tvHintHDL = (TextView) view.findViewById(R.id.tvHDL);
            messageFragment = new MessageDialogFragment();

            //registering btnSubmit with onclicklistener
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // get values from fields and application level constants for request
                    String cholFromUI = txtChol.getText().toString();
                    String sbpFromUI = txtSBP.getText().toString();
                    String hdlFromUI = txtHDL.getText().toString();
                    if(validateUI(cholFromUI, sbpFromUI, hdlFromUI)) {
                        int chol = Integer.parseInt(cholFromUI);
                        int sbp = Integer.parseInt(sbpFromUI);
                        int hdl = Integer.parseInt(hdlFromUI);
                        int age = QuitSmokeClientUtils.getAge();
                        final String gender = QuitSmokeClientUtils.getGender();
                        final boolean isTreated = false;
                        // construct UI entity
                        calculateFrsEntity = new CalculateFrsEntity(age, gender, chol, hdl, sbp, isTreated);

                        CalculateFrsFactorial registerFactorial = new CalculateFrsFactorial(getActivity(), calculateFrsEntity);
                        registerFactorial.execute();
                    } else {
                        CalculateFRSErrorFragment newFragment = new CalculateFRSErrorFragment();
                        Bundle bundle = new Bundle();
                        // pass validation result to dialog
                        bundle.putBoolean("isSBPValid", isSBPValid);
                        bundle.putBoolean("isCholValid", isCholValid);
                        bundle.putBoolean("isHDLValid", isHDLValid);
                        newFragment.setArguments(bundle);
                        newFragment.show(myContext.getSupportFragmentManager(), "calculateFRS");
                    }
                }
            });
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }

        // set hint dialog for chol
        tvHintChol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                // pass message value to dialog
                bundle.putString("message", getActivity().getResources().getString(R.string.hint_chol));
                messageFragment.setArguments(bundle);
                messageFragment.show(myContext.getSupportFragmentManager(), "showHintChol");
            }
        });

        // set hint dialog for SBP
        tvHintSBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                // pass message value to dialog
                bundle.putString("message", getActivity().getResources().getString(R.string.hint_sbp));
                messageFragment.setArguments(bundle);
                messageFragment.show(myContext.getSupportFragmentManager(), "showHintSBP");
            }
        });

        // set hint dialog for HDL
        tvHintHDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                // pass message value to dialog
                bundle.putString("message", getActivity().getResources().getString(R.string.hint_hdl));
                messageFragment.setArguments(bundle);
                messageFragment.show(myContext.getSupportFragmentManager(), "showHintHDL");
            }
        });
    }

    private boolean validateUI(String chol, String sbp, String hdl) {
        boolean isValid = false;
        // check chol
        isCholValid = chol != null && !chol.isEmpty();
        if (isCholValid)
            isCholValid = Integer.parseInt(chol) >= 140 && Integer.parseInt(chol) <= 300;
        // check sbp
        isSBPValid = sbp != null && !sbp.isEmpty();
        if (isSBPValid)
            isSBPValid = Integer.parseInt(sbp) >= 105 && Integer.parseInt(sbp) <= 200;
        // check hdl
        isHDLValid = hdl != null && !hdl.isEmpty();
        if (isHDLValid)
            isHDLValid = Integer.parseInt(hdl) >= 30 && Integer.parseInt(hdl) <= 150;

        isValid = isCholValid && isSBPValid && isHDLValid;
        return isValid;
    }
}
