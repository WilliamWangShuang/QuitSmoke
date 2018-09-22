package clientservice.factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.PlanEntity;
import clientservice.webservice.InteractWebservice;

public class GetCurrentPlanFactorial extends AsyncTask<Void, Void, String> {
    private Activity smokerMainActivity;
    private String uid;
    private boolean isPartnerSet;
    private CircleProgressBar mCustomProgressBar;
    private PlanEntity currentPlan;
    private boolean isRequestFromSmokerFragement;
    public IUpdatePartnerAsyncResponse delegate = null;

    public GetCurrentPlanFactorial(Activity smokerMainActivity, String uid, CircleProgressBar mCustomProgressBar, boolean isRequestFromSmokerFragement) {
        this.uid = uid;
        this.isRequestFromSmokerFragement = isRequestFromSmokerFragement;
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
            // do server side validation check if the smoker has set a supporter
            isPartnerSet = isRequestFromSmokerFragement ? InteractWebservice.isSupporterSet() : true;
            if (isPartnerSet) {
                currentPlan = InteractWebservice.getProceedingPlan(uid);
                Log.d("QuitSmokeDebug", "currentPlan is null:" + (currentPlan == null));
                if (currentPlan != null) {
                    realAmount = currentPlan.getRealAmount();
                    h.sendEmptyMessage(0);
                } else {
                    return QuitSmokeClientConstant.INDICATOR_NO_PLAN;
                }
            } else {
                // if current user not set supporter yet. Set result empty
                Log.d("QuitSmokeDebug", "Partner not set..");
                return QuitSmokeClientConstant.INDICATOR_N;
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
                String encouragement = currentPlan.getEncouragement();
                QuitSmokeClientUtils.setEncouragement(encouragement);
                mCustomProgressBar.setVisibility(View.VISIBLE);
                int realAmount = currentPlan.getRealAmount();
                int targetAmount = currentPlan.getTargetAmount();
                int progress = targetAmount == 0 ? 0 : (int)(realAmount * 100 / targetAmount);
                QuitSmokeClientUtils.simulateProgress(mCustomProgressBar, progress);
            } else {
                Toast.makeText(smokerMainActivity, "Exception occurred when create plan. Try again. If not work, remove the shit app.", Toast.LENGTH_LONG).show();
            }
        }
    };
}