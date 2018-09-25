package clientservice.webservice.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.william.quitsmokeappclient.Interface.ILoadNoSmokePlaceAsyncResponse;

import java.util.List;

import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.db.QuitSmokeDbUtility;
import clientservice.entities.NoSmokePlace;
import clientservice.factory.LoadNoSmokePlaceFactorial;
import clientservice.factory.ResetSmokerPointFactorial;
import clientservice.webservice.InteractWebservice;

public class ResetStreakReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;
    private ResetSmokerPointFactorial resetSmokerPointFactorial;
    private SharedPreferences sharedPreferences;

    public ResetStreakReceiver() {}

    public ResetStreakReceiver(Context context){
        Log.d("QuitSmokeDebug", "start constructor of ResetStreakReceiver");
        // Initial alarm manager used to set repeat clock
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Define which intent to be broadcast to context
        i = new Intent(context, ResetStreakReceiver.class);
        // get current pending board cast intent in context
        pi = PendingIntent.getBroadcast(context,0, i,0);
        // Set alarm do the job of the intent  AlarmManager.INTERVAL_HALF_HOUR
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),AlarmManager.INTERVAL_DAY, pi);

        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        // set isFirstLaunch false
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", false);
        editor.putString(QuitSmokeClientConstant.WS_JSON_UPDATE_PARTNER_KEY_SMOKER_NODE_NAME, QuitSmokeClientUtils.getSmokerNodeName());
        editor.commit();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("QuitSmokeDebug", "start reset streak indicator");
        try {
            /*
            The logic is: If there is no indicator stored in SharedPreference, initialize indicator as value of 'false' when smoker login. Otherwise, use the value stored.
                          When smoker clicks panic button, change value to 'true'. Check the value every 24H, if 'true', reset point of this smoker as 0, otherwise,
                          increment 1 to the original point. Later, the streak will be calculate by the point of this user. Besides, reset indicator in shared preference to
                          'false' every 24H.
            */
            // check shared preference to see if there is indicator is already set for indicating streaking is broken.
            sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            boolean isStreakBroken = sharedPreferences.getBoolean("isStreakBroken", false);
            // if it is true, reset this smoker's point to 0 and reset indicator in shared preference to 'false'. Otherwise, increment it by 1
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (isStreakBroken) {
                // reset indicator in shared preference to 'false'
                editor.putBoolean("isStreakBroken", false);
                // reset this smoker's point to 0.
                resetSmokerPointFactorial = new ResetSmokerPointFactorial(true, context);
            } else {
                // add 1 to original point.
                resetSmokerPointFactorial = new ResetSmokerPointFactorial(false, context);
            }
//            editor.putBoolean("isFirstLaunch", true);
            editor.commit();
            resetSmokerPointFactorial.execute();

        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }
    }
}
