package ClientService.Factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.william.quitsmokeappclient.MainActivity;
import com.example.william.quitsmokeappclient.R;;import ClientService.Entities.CalculateFrsEntity;
import ClientService.Entities.UserInfoEntity;
import ClientService.QuitSmokeClientUtils;
import ClientService.webservice.QuitSmokeUserWebservice;
import ClientService.webservice.QuitSmokerReportWebservice;

public class CalculateFrsFactorial extends AsyncTask<Void, Void, Void> {
    private Activity calculateFrsActivity;
    private CalculateFrsEntity calculateFrsEntity;
    private String calculateResult;

    public CalculateFrsFactorial(Activity calculateFrsActivity, CalculateFrsEntity calculateFrsEntity) {
        this.calculateFrsActivity = calculateFrsActivity;
        this.calculateFrsEntity = calculateFrsEntity;
        calculateResult = "";
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // call backend service method to get calculation result
            calculateResult = QuitSmokerReportWebservice.calculateFRS(calculateFrsEntity);
            h.sendEmptyMessage(0);
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            h.sendEmptyMessage(1);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
        Log.d("QuitSmokeDebug", "calculate Framingham Risk Score finish.");
    }

    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                // calculate successfully, update UI result textView
                ((TextView)calculateFrsActivity.findViewById(R.id.tvRiskResult)).setText(calculateResult + "%");
            } else {
                Toast.makeText(calculateFrsActivity, calculateFrsActivity.getResources().getString(R.string.error_msg), Toast.LENGTH_LONG);
            }
        }
    };
}
