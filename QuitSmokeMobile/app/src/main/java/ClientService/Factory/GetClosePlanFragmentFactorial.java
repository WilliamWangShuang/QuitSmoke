package clientservice.factory;

import android.os.AsyncTask;
import android.util.Log;
import com.example.william.quitsmokeappclient.Interface.IGetPendingPlanResultAsyncResponse;
import java.util.ArrayList;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.PlanEntity;
import clientservice.webservice.InteractWebservice;

public class GetClosePlanFragmentFactorial extends AsyncTask<Void, Void, ArrayList<PlanEntity>> {
    private boolean isSupporterHistoryView;
    private ArrayList<PlanEntity> resultFromWS;
    public IGetPendingPlanResultAsyncResponse delegate = null;

    public GetClosePlanFragmentFactorial() {}

    public GetClosePlanFragmentFactorial(boolean isSupporterHistoryView) {
        this.isSupporterHistoryView = isSupporterHistoryView;
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
            if (isSupporterHistoryView)
                resultFromWS = InteractWebservice.getClosePlanBySupporterEmail(QuitSmokeClientUtils.getEmail());
            else
                resultFromWS = InteractWebservice.getClosePlanBySmokerUid(QuitSmokeClientUtils.getUid());
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }
        return resultFromWS;
    }
}
