package com.example.william.quitsmokeappclient.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.Interface.IGetPendingPlanResultAsyncResponse;
import com.example.william.quitsmokeappclient.Interface.IPlanRecycleItemClick;
import com.example.william.quitsmokeappclient.R;
import java.util.ArrayList;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.PlanEntity;
import clientservice.factory.ClosePlanRecycleViewAdapter;
import clientservice.factory.GetClosePlanFragmentFactorial;

public class PlanHistoryFragment extends Fragment implements IGetPendingPlanResultAsyncResponse {

    private ArrayList<PlanEntity> resultFromFactory;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View myRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_history_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get root view
        myRootView = getView();
        // get current user email
        String email = QuitSmokeClientUtils.getEmail();
        GetClosePlanFragmentFactorial getClosePlanFragmentFactorial =  new GetClosePlanFragmentFactorial(QuitSmokeClientUtils.getUid());
        getClosePlanFragmentFactorial.delegate = this;
        getClosePlanFragmentFactorial.execute();
    }

    @Override
    public void processFinish(ArrayList<PlanEntity> reponseResult) {
        resultFromFactory = reponseResult;
        // if result from factory is not null, initialize UI fields value
        Log.d("QuitSmokeDebug", "result from GetClosePlanFragmentFactorial:" + (reponseResult == null));
        if(resultFromFactory != null) {
            mRecyclerView = (RecyclerView)myRootView.findViewById(R.id.rv_close_plan);
            mRecyclerView.setHasFixedSize(true);

            // set layout manager
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new ClosePlanRecycleViewAdapter(resultFromFactory, new IPlanRecycleItemClick() {
                @Override
                public void onItemClick(PlanEntity item) {
                    Log.d("QuitSmokeDebug","====Recycle view item on Close Plan Fragment onclick triggered====");
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            ((TextView)myRootView.findViewById(R.id.tvStatusSupporter)).setText("No new plan created by your partner.");
        }
    }
}
