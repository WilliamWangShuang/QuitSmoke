package clientservice.factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.quitsmoke.william.quitsmokeappclient.R;
import clientservice.entities.CalculateFrsEntity;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.QuitSmokerReportWebservice;

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
                int resulInt = Integer.parseInt(calculateResult);
                if (resulInt > 17)
                    calculateResult = "Over 30%";
                else if (resulInt == 16)
                    calculateResult = "25%";
                else if (resulInt == 15)
                    calculateResult = "20%";
                else if (resulInt == 14)
                    calculateResult = "16%";
                else if (resulInt == 13)
                    calculateResult = "12%";
                else if (resulInt == 12)
                    calculateResult = "10%";
                else if (resulInt == 11)
                    calculateResult = "8%";
                else if (resulInt == 10)
                    calculateResult = "6%";
                else if (resulInt == 9)
                    calculateResult = "5%";
                else if (resulInt == 8)
                    calculateResult = "4%";
                else if (resulInt == 7)
                    calculateResult = "3%";
                else if (resulInt == 5 || resulInt == 6)
                    calculateResult = "2%";
                else if (resulInt >= 1 && resulInt <= 4)
                    calculateResult = "1%";
                else if (resulInt == 0)
                    calculateResult = " <1%";
                else
                    calculateResult = "Unknown";
                ((TextView)calculateFrsActivity.findViewById(R.id.tvRiskResult)).setText("10-Year Risk:"+ calculateResult);
            } else {
                Toast.makeText(calculateFrsActivity, calculateFrsActivity.getResources().getString(R.string.error_msg), Toast.LENGTH_LONG);
            }
        }
    };
}
