package clientservice.factory;

import android.os.AsyncTask;
import android.util.Log;
import com.example.william.quitsmokeappclient.Interface.IGetPendingPlanResultAsyncResponse;
import java.util.ArrayList;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.PlanEntity;
import clientservice.webservice.InteractWebservice;

public class GetClosePlanFragmentFactorial extends AsyncTask<Void, Void, ArrayList<PlanEntity>> {
    private String smokerUid;
    private ArrayList<PlanEntity> resultFromWS;
    public IGetPendingPlanResultAsyncResponse delegate = null;

    public GetClosePlanFragmentFactorial() {}

    public GetClosePlanFragmentFactorial(String smokerUid) {
        this.smokerUid = smokerUid;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("QuitSmokeDebug", "GetClosePlanFragmentFactorial start.");
    }

    @Override
    protected void onPostExecute(ArrayList<PlanEntity> result) {
        super.onPostExecute(result);
        Log.d("QuitSmokeDebug", "GetClosePlanFragmentFactorial end.");
        delegate.processFinish(result);
    }

    @Override
    protected ArrayList<PlanEntity> doInBackground(Void... voids) {
        // construct request param json
        try {
            resultFromWS = InteractWebservice.getClosePlanBySmokerUid(smokerUid);
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }
        return resultFromWS;
    }
}
