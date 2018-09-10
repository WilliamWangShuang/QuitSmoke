package clientservice.factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.william.quitsmokeappclient.Fragments.CreatePlanErrorFragement;
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.MainActivity;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.PlanEntity;
import clientservice.webservice.InteractWebservice;

public class GetCurrentPlanFactorial extends AsyncTask<Void, Void, String> {
    private Activity smokerMainActivity;
    private String uid;
    private CircleProgressBar mCustomProgressBar;
    private PlanEntity currentPlan;
    public IUpdatePartnerAsyncResponse delegate = null;

    public GetCurrentPlanFactorial(Activity smokerMainActivity, String uid, CircleProgressBar mCustomProgressBar) {
        this.uid = uid;
        this.smokerMainActivity = smokerMainActivity;
        this.mCustomProgressBar = mCustomProgressBar;
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "get current proceeding plan starts.");
    }

    @Override
    protected String doInBackground(Void... params) {
        int realAmount = 0;
        try {
            currentPlan = InteractWebservice.getProceedingPlan(uid);
            Log.d("QuitSmokeDebug", "currentPlan is null:" + (currentPlan == null));
            if (currentPlan != null) {
                realAmount = currentPlan.getRealAmount();
                h.sendEmptyMessage(0);
            } else {
                h.sendEmptyMessage(1);
            }
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            h.sendEmptyMessage(2);
        }

        return "" + realAmount;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("QuitSmokeDebug", "current proceeding plan finish.");
        delegate.processFinish(result);
    }

    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                mCustomProgressBar.setVisibility(View.VISIBLE);
                int realAmount = currentPlan.getRealAmount();
                int targetAmount = currentPlan.getTargetAmount();
                int progress = (int)(realAmount * 100 / targetAmount);
                QuitSmokeClientUtils.simulateProgress(mCustomProgressBar, progress);
            } else if (msg.what == 1) {
                mCustomProgressBar.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(smokerMainActivity, "Exception occurred when create plan. Try again. If not work, remove the shit app.", Toast.LENGTH_LONG).show();
            }
        }
    };
}