package clientservice.factory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import com.example.william.quitsmokeappclient.Fragments.SmokerMainFragment;
import com.example.william.quitsmokeappclient.R;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.InteractWebservice;

public class AddSmokeAmountFactorial extends AsyncTask<Void, Void, Void> {
    private String uid;
    private FragmentManager fragmentManager;
    private Context context;

    public AddSmokeAmountFactorial(String uid, FragmentManager fragmentManager, Context context) {
        this.uid = uid;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "add smoke amount starts.");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // call ws to add amount
            boolean result = InteractWebservice.addRealAmount(uid);
            if (result)
                h.sendEmptyMessage(0);
        } catch (Exception ex) {
            QuitSmokeClientUtils.getExceptionInfo(ex);
            h.sendEmptyMessage(1);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("QuitSmokeDebug", "add smoke amount end.");
    }

    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                // set isStreakBroken in SharedPreference to 'true' to break streak
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isStreakBroken", true);
                editor.commit();
                // go back to smoker main page
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SmokerMainFragment()).commit();
            } else {
                Toast.makeText(context, "Unexpect error occurs.", Toast.LENGTH_SHORT);
            }

        }
    };
}
