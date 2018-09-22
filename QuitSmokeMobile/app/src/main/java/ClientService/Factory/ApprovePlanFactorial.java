package clientservice.factory;

import android.os.AsyncTask;
import android.util.Log;
import com.example.william.quitsmokeappclient.Interface.IApprovePlanAsyncResponse;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.InteractWebservice;

public class ApprovePlanFactorial extends AsyncTask<Void, Void, Boolean> {
    private String uid;
    private int targetAmount;
    private boolean isSucc;
    public IApprovePlanAsyncResponse delegate = null;

    public ApprovePlanFactorial(String uid, int targetAmount) {
        this.uid = uid;
        this.targetAmount = targetAmount;
        isSucc = false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("QuitSmokeDebug", "ApprovePlanFactorial start.");
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
        Log.d("QuitSmokeDebug", "ApprovePlanFactorial end.");
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            isSucc = InteractWebservice.approvePlan(uid, targetAmount);
            Log.d("QuitSmokeDebug","is approve plan success:" + isSucc);
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }

        return isSucc;
    }
}
