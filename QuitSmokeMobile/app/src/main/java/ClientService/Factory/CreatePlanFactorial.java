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
import android.widget.Toast;
import com.example.william.quitsmokeappclient.Fragments.CreatePlanErrorFragement;
import com.example.william.quitsmokeappclient.MainActivity;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.InteractWebservice;

public class CreatePlanFactorial extends AsyncTask<Void, Void, Void> {
    private Activity createPlanActivity;
    private CreatePlanErrorFragement createPlanErrorFragment;
    private boolean isPartnerSet;
    private FragmentManager fragmentManager;
    private int targetAmount;

    public CreatePlanFactorial(Activity createPlanActivity, FragmentManager fragmentManager, int targetAmount) {
        this.fragmentManager = fragmentManager;
        this.createPlanActivity = createPlanActivity;
        this.targetAmount = targetAmount;
        createPlanErrorFragment = new CreatePlanErrorFragement();
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "create plan starts.");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // do server side validation check if the smoker has set a supporter
            isPartnerSet = InteractWebservice.isSupporterSet();
            if (isPartnerSet) {
                String newPlanNodeName = InteractWebservice.createPlan(targetAmount);
                QuitSmokeClientUtils.setPlanNodeName(newPlanNodeName);
                h.sendEmptyMessage(0);
            } else {
                h.sendEmptyMessage(1);
            }
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            h.sendEmptyMessage(2);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d("QuitSmokeDebug", "create plan finish.");
    }

    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                Intent intent = new Intent(createPlanActivity, MainActivity.class);
                createPlanActivity.startActivityForResult(intent, 1);
            } else if (msg.what == 1) {
                // get validation value to bundle to pass to error dialog entity
                Bundle bundle = new Bundle();
                // set empty input target amount error message indicator is true. This is for hiding empty input error message during server side validation phase
                bundle.putBoolean("isTargetNoValid", true);
                bundle.putBoolean("isPartberSet", isPartnerSet);
                createPlanErrorFragment.setArguments(bundle);
                createPlanErrorFragment.show(fragmentManager, "no partner");
            } else {
                Toast.makeText(createPlanActivity, "Exception occurred when create plan. Try again. If not work, remove the shit app.", Toast.LENGTH_LONG).show();
            }
        }
    };
}