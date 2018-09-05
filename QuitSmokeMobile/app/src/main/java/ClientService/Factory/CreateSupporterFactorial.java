package clientservice.factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.MainActivity;
import com.example.william.quitsmokeappclient.R;
import clientservice.entities.UpdatePartnerEntity;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.QuitSmokeUserWebservice;

public class CreateSupporterFactorial extends AsyncTask<Void, Void, Void> {
    private Activity createSupporterActivity;
    private String supporterEmail;
    private boolean isSupporterExist;
    private TextView tvErrorMsg;

    public CreateSupporterFactorial(Activity registerActivity, String supporterEmail) {
        this.createSupporterActivity = registerActivity;
        this.supporterEmail = supporterEmail;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        tvErrorMsg = (TextView)createSupporterActivity.findViewById(R.id.lblAddSupporterMsg);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("QuitSmokeDebug","****create supporter logic start****");
        try {
            isSupporterExist = QuitSmokeUserWebservice.checkUserExistByEmail(supporterEmail);
            if (isSupporterExist) {
                if (!supporterEmail.equals(QuitSmokeClientUtils.getEmail())) {
                    boolean isUpdateSucc = false;
                    // construct request json object
                    UpdatePartnerEntity entity = new UpdatePartnerEntity(QuitSmokeClientUtils.getSmokerNodeName(), supporterEmail);
                    // call ws to do update
                    isUpdateSucc = QuitSmokeUserWebservice.updatePartner(entity);
                    // send behavior according to ws result
                    if (isUpdateSucc) {
                        h.sendEmptyMessage(0);
                    } else {
                        h.sendEmptyMessage(2);
                    }
                } else {
                    h.sendEmptyMessage(3);
                }

            } else {
                // if user with the email not found, show error message
                h.sendEmptyMessage(1);
            }
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            h.sendEmptyMessage(2);
        }
        Log.d("QuitSmokeDebug","****create supporter logic end****");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
        Log.d("QuitSmokeDebug", "create supporter finish.");
    }

    // create a handler to toast message on main thread according to the post result
    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                tvErrorMsg.setText("");
                // go to main activity
                Intent intent = new Intent(createSupporterActivity, MainActivity.class);
                createSupporterActivity.startActivityForResult(intent, 1);
            } else if(msg.what == 1) {
                // show partner not exist error message
                tvErrorMsg.setText(createSupporterActivity.getResources().getString(R.string.supporter_not_found));
            } else if(msg.what == 3) {
                // show partner cannot be smoker himself
                tvErrorMsg.setText(createSupporterActivity.getResources().getString(R.string.supporter_is_smoker));
            } else {
                // show exception error message
                tvErrorMsg.setText(createSupporterActivity.getResources().getString(R.string.error_msg));
            }

        }
    };
}
