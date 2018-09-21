package clientservice.factory;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;

import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.UpdatePartnerEntity;
import clientservice.webservice.InteractWebservice;
import clientservice.webservice.QuitSmokeUserWebservice;

public class UpdateEncouragementFactorial extends AsyncTask<Void, Void, String> {
    private String smokerUid;
    private String newEncourage;
    public IUpdatePartnerAsyncResponse delegate = null;
    private Activity planDetailActivity;

    public UpdateEncouragementFactorial(String smokerUid, String newEncourage, Activity planDetailActivity) {
        this.smokerUid = smokerUid;
        this.newEncourage = newEncourage;
        this.planDetailActivity = planDetailActivity;
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "update encouragement starts.");
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        try {
            boolean resultFromWS = InteractWebservice.updateEncouragement(smokerUid, newEncourage);
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
        Log.d("QuitSmokeDebug", "update encouragement finish.");
        delegate.processFinish(result);
    }
}
