package ClientService.Factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.william.quitsmokeappclient.MainActivity;
import com.example.william.quitsmokeappclient.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import ClientService.Entities.UserInfoEntity;
import ClientService.QuitSmokeClientUtils;
import ClientService.webservice.QuitSmokeUserWebservice;

public class RegisterFactorial extends AsyncTask<Void, Void, Void> {
    // user email variables
    private String email;
    // email text view
    private TextView tvEmail;
    // register activity
    private Activity activity;
    // domain entity
    private UserInfoEntity registerInfoUI;
    boolean isSucc;

    public RegisterFactorial(Activity activity, TextView tvEmail, UserInfoEntity registerInfoUI) {
        this.registerInfoUI = registerInfoUI;
        this.activity = activity;
        this.tvEmail = tvEmail;
        this.email = tvEmail.getText().toString();
        isSucc = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // check if a user with same email is already exist
        try {
            isSucc = QuitSmokeUserWebservice.saveRegisterResident(registerInfoUI);
            if (isSucc) {
                h.sendEmptyMessage(0);
            } else {
                h.sendEmptyMessage(1);
            }
        } catch (Exception ex) {
            Log.e("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
        Log.d("QuitSmokeDebug", "register finish.");

    }

    // create a handler to toast message on main thread according to the post result

    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                try {
                    Toast.makeText(activity, activity.getResources().getString(R.string.register_success), Toast.LENGTH_LONG);
                    // set resident info to application level
                    QuitSmokeClientUtils.setEmail(registerInfoUI.getEmail());
                    QuitSmokeClientUtils.setPassword(registerInfoUI.getPassword());

                    // clear text field background color and error message
                    tvEmail.setBackgroundColor(activity.getResources().getColor(R.color.whiteBg));
                    ((TextView)activity.findViewById(R.id.lblEmailErrorMsg)).setText("");
                    //TODO: go to main activity
//                        Intent intent = new Intent(activity, MainActivity.class);
//                        activity.startActivityForResult(intent, 1);
                } catch (Exception ex) {
                    Log.e("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
                    Toast.makeText(activity, activity.getResources().getString(R.string.register_throw_exception), Toast.LENGTH_LONG);
                }
            } else if(msg.what == 1) {
                // if user with same email exist, server side validation not passed
                Log.e("QuitSmokeDebug", activity.getResources().getString(R.string.register_email_exist_msg));
                tvEmail.setBackgroundColor(activity.getResources().getColor(R.color.errorBackgound));
                ((TextView)activity.findViewById(R.id.lblEmailErrorMsg)).setText(activity.getResources().getString(R.string.register_email_exist_msg));
            }
        }
    };
}
