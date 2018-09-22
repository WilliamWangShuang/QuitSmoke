package com.example.william.quitsmokeappclient.Fragments;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.Interface.IGetPendingPlanResultAsyncResponse;
import com.example.william.quitsmokeappclient.Interface.IPlanRecycleItemClick;
import com.example.william.quitsmokeappclient.R;
import java.util.ArrayList;
import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.PlanEntity;
import clientservice.factory.InitialPartnerFragmentFactorial;
import clientservice.factory.PendingPlanRecycleViewAdapter;

public class PartnerMainFragment extends Fragment implements IGetPendingPlanResultAsyncResponse {

    private ArrayList<PlanEntity> resultFromFactory;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ApprovePlanDialogFragment approvePlanDialogFragment;
    private FragmentActivity myContext;
    private View myRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.partner_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get root view
        myRootView = getView();
        // get current user email
        String email = QuitSmokeClientUtils.getEmail();
        InitialPartnerFragmentFactorial initialPartnerFragmentFactorial =  new InitialPartnerFragmentFactorial(email, false);
        initialPartnerFragmentFactorial.delegate = this;
        initialPartnerFragmentFactorial.execute();
    }

    @Override
    public void processFinish(ArrayList<PlanEntity> reponseResult) {
        resultFromFactory = reponseResult;
        // if result from factory is not null, initialize UI fields value
        Log.d("QuitSmokeDebug", "result from InitialPartnerFragmentFactorial:" + (reponseResult == null));
        if(resultFromFactory != null) {
            mRecyclerView = (RecyclerView)myRootView.findViewById(R.id.rv_pending_plan);
            mRecyclerView.setHasFixedSize(true);

            // set layout manager
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new PendingPlanRecycleViewAdapter(resultFromFactory, new IPlanRecycleItemClick() {
                @Override
                public void onItemClick(PlanEntity item) {
                    Log.d("QuitSmokeDebug","====Recycle view item onclick triggered====");
                    if (QuitSmokeClientConstant.STATUS_PENDING.equals(item.getStatus())) {
                        approvePlanDialogFragment = new ApprovePlanDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("uid", item.getUid());
                        args.putInt("pos", QuitSmokeClientUtils.getPlanPositionInPlanList(item, resultFromFactory));
                        args.putParcelableArrayList("resource", resultFromFactory);
                        myContext = (FragmentActivity)getContext();
                        approvePlanDialogFragment.setArguments(args);
                        approvePlanDialogFragment.show(myContext.getSupportFragmentManager(), "approvePlan");
                    } else {
                        PlanDetailFragment planDetailFragment = new PlanDetailFragment();
                        Bundle args = new Bundle();
                        args.putString("uid", item.getUid());
                        planDetailFragment.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, planDetailFragment).commit();
                    }
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            ((TextView)myRootView.findViewById(R.id.tvStatusSupporter)).setText("No new plan created by your partner.");
        }
    }
}
