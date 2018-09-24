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

import org.json.JSONException;

import java.io.IOException;

import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.InteractWebservice;

public class ResetSmokerPointFactorial extends AsyncTask<Void, Void, Void> {
    private boolean isReset;

    public ResetSmokerPointFactorial(boolean isReset) {
        this.isReset = isReset;
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "ResetSmokerPointFactorial starts.");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            InteractWebservice.updatePoint(QuitSmokeClientUtils.getSmokerNodeName(), isReset);
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