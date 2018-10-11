package clientservice.factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.quitsmoke.william.quitsmokeappclient.Fragments.CreatePlanErrorFragement;
import com.quitsmoke.william.quitsmokeappclient.MainActivity;

import org.json.JSONException;

import java.io.IOException;

import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.InteractWebservice;

public class ResetSmokerPointFactorial extends AsyncTask<Void, Void, Void> {
    private boolean isReset;
    private SharedPreferences sharedPreferences;
    private Context context;

    public ResetSmokerPointFactorial(boolean isReset, Context context) {
        this.isReset = isReset;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "ResetSmokerPointFactorial starts.");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            String smokerNodeName = sharedPreferences.getString(QuitSmokeClientConstant.WS_JSON_UPDATE_PARTNER_KEY_SMOKER_NODE_NAME, QuitSmokeClientUtils.getSmokerNodeName());
            InteractWebservice.updatePoint(smokerNodeName, isReset);
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d("QuitSmokeDebug", "ResetSmokerPointFactorial finish.");
    }
}