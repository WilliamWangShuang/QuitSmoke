package ClientService.Factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.william.quitsmokeappclient.Fragments.CreatePlanErrorFragement;
import com.example.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.example.william.quitsmokeappclient.MainActivity;
import com.example.william.quitsmokeappclient.R;

import ClientService.Entities.UpdatePartnerEntity;
import ClientService.QuitSmokeClientUtils;
import ClientService.webservice.InteractWebservice;
import ClientService.webservice.QuitSmokeUserWebservice;

public class UpdatePartnerFactorial extends AsyncTask<Void, Void, String> {
    private EditText txtSetPartner;
    private Fragment parentFragement;
    private Activity createPlanActivity;
    public IUpdatePartnerAsyncResponse delegate = null;

    public UpdatePartnerFactorial(EditText txtSetPartner, Fragment parentFragement, Activity createPlanActivity) {
        this.txtSetPartner = txtSetPartner;
        this.parentFragement = parentFragement;
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
                    boolean isUpdateSucc = false;
                    // construct request json object
                    UpdatePartnerEntity entity = new UpdatePartnerEntity(QuitSmokeClientUtils.getSmokerNodeName(), partnerEmail);
                    // call ws to do update
                    isUpdateSucc = QuitSmokeUserWebservice.updatePartner(entity);
                    // send behavior according to ws result
                    if (isUpdateSucc) {
                        TextView tvParentPartner = (TextView)parentFragement.getView().findViewById(R.id.tvParternerSet);
                        tvParentPartner.setText(createPlanActivity.getResources().getString(R.string.partner_set));
                    }
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
