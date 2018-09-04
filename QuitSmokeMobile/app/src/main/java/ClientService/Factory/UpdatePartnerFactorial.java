package ClientService.Factory;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.R;
import ClientService.Entities.UpdatePartnerEntity;
import ClientService.QuitSmokeClientUtils;
import ClientService.webservice.QuitSmokeUserWebservice;

public class UpdatePartnerFactorial extends AsyncTask<Void, Void, String> {
    private EditText txtSetPartner;
    private Activity createPlanActivity;
    public IUpdatePartnerAsyncResponse delegate = null;
    private boolean isUpdateSucc;

    public UpdatePartnerFactorial(EditText txtSetPartner, Activity createPlanActivity) {
        this.txtSetPartner = txtSetPartner;
        this.createPlanActivity = createPlanActivity;
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "update partner starts.");
    }

    @Override
    protected String doInBackground(Void... params) {
        String partnerEmail = txtSetPartner.getText().toString();
        String result = "";
        try {
            if(QuitSmokeUserWebservice.checkUserExistByEmail(partnerEmail)) {
                if (!partnerEmail.equals(QuitSmokeClientUtils.getEmail())) {
                    isUpdateSucc = false;
                    // construct request json object
                    UpdatePartnerEntity entity = new UpdatePartnerEntity(QuitSmokeClientUtils.getSmokerNodeName(), partnerEmail);
                    // call ws to do update
                    isUpdateSucc = QuitSmokeUserWebservice.updatePartner(entity);
                    // get succ message text view
                    if (isUpdateSucc)
                        result = createPlanActivity.getResources().getString(R.string.partner_set);
                } else {
                    result = createPlanActivity.getResources().getString(R.string.supporter_is_smoker);
                }
            } else {
                result = createPlanActivity.getResources().getString(R.string.supporter_not_found);
            }
        } catch (Exception ex) {
            QuitSmokeClientUtils.getExceptionInfo(ex);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("QuitSmokeDebug", "update partner finish.");
        delegate.processFinish(result);
    }
}
