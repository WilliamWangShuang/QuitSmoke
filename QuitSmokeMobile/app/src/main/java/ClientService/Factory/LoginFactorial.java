package clientservice.factory;

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
import com.example.william.quitsmokeappclient.R;
import clientservice.entities.UserInfoEntity;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.QuitSmokeUserWebservice;

public class LoginFactorial extends AsyncTask<Void, Void, Void> {
    private String name;
    private String email;
    private String pwd;
    private Activity loginActivity;
    // if account info exist in preference
    private boolean isPreferenceExist;
    // indicator if find user successfully
    private boolean isFound;
    // indicator if error occur
    private boolean isError;
    // user info variable
    UserInfoEntity userProfile;

    public LoginFactorial(Activity loginActivity, String email, String pwd, boolean isPreferenceExist) {
        this.email = email;
        this.pwd = pwd;
        this.loginActivity = loginActivity;
        this.isPreferenceExist = isPreferenceExist;
    }

    @Override
        protected void onPreExecute() {
        super.onPreExecute();
        isFound = false;
        isError = false;
        userProfile = null;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("QuitSmokeDebug","****login logic start****");
        // encrypt email
        String encryptPwdFromUI = isPreferenceExist ? pwd : QuitSmokeClientUtils.encryptPwd(pwd);

        // initial resident info by calling webservice
        try {
            userProfile = QuitSmokeUserWebservice.findUserByEmailAndPwd(email, encryptPwdFromUI);
            Log.d("QuitSmokeDebug", "is user profile null:" + (userProfile == null));
            isFound = userProfile != null;
            isError = false;
        } catch (NullPointerException ex) {
            isFound = false;
            isError = false;
            Log.d("QuitSmokeDebug", "No user found by input email and password.");
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        } catch (Exception ex) {
            isFound = false;
            isError = true;
            Log.e("QuitSmokeDebug", "Error occured during login: " + ex.getMessage());
            Log.e("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }

        // if succeed to find user. initial application-level value and go to main activity
        if (isFound) {
            // set application-level latitude and longtitude for current user and postcode for retrieving local weather
            QuitSmokeClientUtils.setName(userProfile.getName());
            QuitSmokeClientUtils.setEmail(email);
            QuitSmokeClientUtils.setPassword(encryptPwdFromUI);
            QuitSmokeClientUtils.setUid(userProfile.getUid());
            QuitSmokeClientUtils.setAge(Integer.parseInt(userProfile.getAge()));
            QuitSmokeClientUtils.setGender(userProfile.getGender());
            QuitSmokeClientUtils.setSmokerNodeName(userProfile.getSmokerNodeName());
            QuitSmokeClientUtils.setIsPartner(userProfile.isPartner());
            QuitSmokeClientUtils.setIsSmoker(userProfile.isSmoker());
            h.sendEmptyMessage(2);
        } else {
            if (isError) {
                // if error happens, toast notification message
                h.sendEmptyMessage(0);
            } else {
                // if no exception, means no resident found by input email and password.
                h.sendEmptyMessage(1);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
        Log.d("QuitSmokeDebug", "login finish.");
    }

    // create a handler to toast message on main thread according to the post result
    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0)
                Toast.makeText(loginActivity, "Exception occurred when sign in. Try again. If not solved, remove the shit app.", Toast.LENGTH_LONG).show();
            else if(msg.what == 1) {
                TextView tvMsg = loginActivity.findViewById(R.id.lblErrorMsg);
                tvMsg.setText("No accound found. Check your usearname and password.");
            } else {
                // refresh message textview
                TextView tvMsg = loginActivity.findViewById(R.id.lblErrorMsg);
                tvMsg.setText("");
                // go to main activity
                Intent intent = new Intent(loginActivity, MainActivity.class);
                loginActivity.startActivityForResult(intent, 1);
            }

        }
    };
}