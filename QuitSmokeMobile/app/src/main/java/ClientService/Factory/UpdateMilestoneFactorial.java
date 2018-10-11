package clientservice.factory;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.quitsmoke.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;

import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.InteractWebservice;

public class UpdateMilestoneFactorial extends AsyncTask<Void, Void, String> {
    private String smokerUid;
    private int milestoneTarget;
    private String reward;
    public IUpdatePartnerAsyncResponse delegate = null;

    public UpdateMilestoneFactorial(String smokerUid, int milestoneTarget, String reward) {
        this.smokerUid = smokerUid;
        this.milestoneTarget = milestoneTarget;
        this.reward = reward;
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "update milestone starts.");
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        try {
            // call REST to update milestone info
            boolean resultFromWS = InteractWebservice.updateMilstone(smokerUid, milestoneTarget, reward);
            if (resultFromWS) {
                result = QuitSmokeClientConstant.INDICATOR_Y;
            } else {
                result = QuitSmokeClientConstant.INDICATOR_N;
            }
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            result = QuitSmokeClientConstant.INDICATOR_N;
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("QuitSmokeDebug", "update milestone finish.");
        delegate.processFinish(result);
    }
}
