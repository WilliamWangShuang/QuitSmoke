package com.example.william.quitsmokeappclient.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.william.quitsmokeappclient.R;

import ClientService.Factory.CreatePlanFactorial;
import ClientService.QuitSmokeClientUtils;

public class CreatePlanFragment extends Fragment {
    private View vCreatePlanFragment;
    private Button btnCreatePlan;
    private String partner_email;
    private int targetAmount;
    private EditText txtTargetAmount;
    private CreatePlanErrorFragement createPlanErrorFragement;
    private FragmentActivity myContext;
    private boolean isTargetNoValid;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vCreatePlanFragment = inflater.inflate(R.layout.fragment_create_plan, container, false);

        return vCreatePlanFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get UI fields
        btnCreatePlan = (Button)getActivity().findViewById(R.id.btnCreatePlan);
        txtTargetAmount = (EditText)getActivity().findViewById(R.id.txtTargetAmount);
        createPlanErrorFragement = new CreatePlanErrorFragement();

        // set create button listener
        btnCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // get user input target amount
                    if (validateUI()) {
                        targetAmount = Integer.parseInt(txtTargetAmount.getText().toString());
                        // call ws to start the create logic
                        CreatePlanFactorial createPlanFactorial = new CreatePlanFactorial(getActivity(), myContext.getSupportFragmentManager(), targetAmount);
                        createPlanFactorial.execute();
                    } else {
                        Bundle bundle = new Bundle();
                        // pass message value to dialog
                        bundle.putBoolean("isTargetNoValid", isTargetNoValid);
                        // set no partner error message indicator is true. This is for hiding no partner error message during UI validation phase
                        bundle.putBoolean("isPartberSet", true);
                        createPlanErrorFragement.setArguments(bundle);
                        createPlanErrorFragement.show(myContext.getSupportFragmentManager(), "createPlan");
                    }
                } catch (Exception ex) {
                    QuitSmokeClientUtils.getExceptionInfo(ex);
                    Log.d("QuitSmokeDebug", "error occur when create plan.\n" + ex);
                }
            }
        });
    }

    private boolean validateUI() {
        boolean isValid = true;
        if (txtTargetAmount.getText().toString() == null || txtTargetAmount.getText().toString().isEmpty())
            isTargetNoValid = false;
        else
            isTargetNoValid = true;

        isValid = isTargetNoValid;
        return isValid;
    }
}
