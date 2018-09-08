package clientservice.factory;

import android.os.AsyncTask;
import android.util.Log;
import com.example.william.quitsmokeappclient.Interface.IGetPendingPlanResultAsyncResponse;
import java.util.ArrayList;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.PlanEntity;
import clientservice.webservice.InteractWebservice;

public class InitialPartnerFragmentFactorial extends AsyncTask<Void, Void, ArrayList<PlanEntity>> {
    private String partnerEmail;
    private ArrayList<PlanEntity> resultFromWS;
    public IGetPendingPlanResultAsyncResponse delegate = null;

    public InitialPartnerFragmentFactorial() {}

    public InitialPartnerFragmentFactorial(String partnerEmail) {
        this.partnerEmail = partnerEmail;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("QuitSmokeDebug", "InitialPartnerFragmentFactorial start.");
    }

    @Override
    protected void onPostExecute(ArrayList<PlanEntity> result) {
        super.onPostExecute(result);
        Log.d("QuitSmokeDebug", "InitialPartnerFragmentFactorial end.");
        delegate.processFinish(result);
    }

    @Override
    protected ArrayList<PlanEntity> doInBackground(Void... voids) {
        // construct request param json
        try {
            resultFromWS = InteractWebservice.getPendingPlanByPartnerEmail(partnerEmail);
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }
        return resultFromWS;
    }
}
