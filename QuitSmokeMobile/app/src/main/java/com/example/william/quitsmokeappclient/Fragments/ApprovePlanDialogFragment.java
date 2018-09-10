package com.example.william.quitsmokeappclient.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.william.quitsmokeappclient.Interface.IApprovePlanAsyncResponse;
import com.example.william.quitsmokeappclient.Interface.IPlanRecycleItemClick;
import com.example.william.quitsmokeappclient.R;
import java.util.ArrayList;
import clientservice.QuitSmokeClientConstant;
import clientservice.entities.PlanEntity;
import clientservice.factory.ApprovePlanFactorial;
import clientservice.factory.PendingPlanRecycleViewAdapter;

public class ApprovePlanDialogFragment extends DialogFragment implements IApprovePlanAsyncResponse {
    private EditText txtTargetAmount;
    private String uid;
    private int amount;
    private ApprovePlanFactorial approvePlanFactorial;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<PlanEntity> resultFromFactory;
    private ApprovePlanDialogFragment approvePlanDialogFragment;
    private FragmentActivity myContext;

    public ApprovePlanDialogFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.approve_plan_dialog, null);
        myContext = (FragmentActivity)getContext();

        // get uid
        uid = getArguments().getString("uid");
        // get view resource data set
        resultFromFactory = getArguments().getParcelableArrayList("resource");
        Log.d("QuitSmokeDebug", "uid from bundle is null:" + (uid == null) + ", result is null:" + (resultFromFactory == null || resultFromFactory.size() <= 0));
        // get text view in dialog
        txtTargetAmount = (EditText)view.findViewById(R.id.txtApprovePlan);
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.rv_pending_plan);
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputAmount = txtTargetAmount.getText().toString();
                        // if supporter not type in a numbear, show error mesgage. Otherwise, approve the plan
                        if (userInputAmount == null || "".equals(userInputAmount)) {
                            Toast.makeText(myContext, "Please enater a number.", Toast.LENGTH_SHORT).show();
                        } else {
                            amount = Integer.parseInt(userInputAmount);
                            // Inflate and set the layout for the dialog
                            approvePlanFactorial = new ApprovePlanFactorial(uid, amount);
                            approvePlanFactorial.delegate = ApprovePlanDialogFragment.this;
                            approvePlanFactorial.execute();
                        }
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ApprovePlanDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void processFinish(boolean reponseResult) {
        if (reponseResult) {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            Log.d("QuitSmokeDebug", "is Recycle view null:" + (mRecyclerView == null));
            mRecyclerView.setHasFixedSize(true);

            // set layout manager
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // find out the updated plan item by uid and update adapter resource
            for (PlanEntity plan : resultFromFactory) {
                if (plan.getUid().equals(uid)) {
                    plan.setStatus(QuitSmokeClientConstant.STATUS_APPROVE);
                    plan.setTargetAmount(amount);
                }
            }
            // specify an adapter (see also next example)
            mAdapter = new PendingPlanRecycleViewAdapter(resultFromFactory, new IPlanRecycleItemClick() {
                @Override
                public void onItemClick(PlanEntity item) {
                    Log.d("QuitSmokeDebug","====Recycle view item onclick triggered====");
                    if (QuitSmokeClientConstant.STATUS_PENDING.equals(item.getStatus())) {
                        approvePlanDialogFragment = new ApprovePlanDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("uid", item.getUid());
                        args.putParcelableArrayList("resource", resultFromFactory);
                        approvePlanDialogFragment.setArguments(args);
                        approvePlanDialogFragment.show(myContext.getSupportFragmentManager(), "approvePlan");
                    } else {
                        Toast.makeText(myContext,"Cannot update unpending plan.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
