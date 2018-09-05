package clientservice.webservice.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.example.william.quitsmokeappclient.R;

public class CheckPlanReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;
    private Context context;
    private NotificationCompat.Builder mBuilder;

    public CheckPlanReceiver() {}

    public CheckPlanReceiver(Context context){
        Log.d("QuitSmokeDebug", "start constructor of CheckPlanReceiver");
        // get context
        this.context = context;
        // Initial alarm manager used to set repeat clock
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Define which intent to be broadcast to context
        i = new Intent(context, CheckPlanReceiver.class);
        // get current pending board cast intent in context
        pi = PendingIntent.getBroadcast(context,0, i,0);
        // Set repeater do the job of the intent every hour   AlarmManager.INTERVAL_HALF_HOUR
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),3000, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("QuitSmokeDebug", "start check plan create receiver");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Log.d("QuitSmokeDebug", "notificationManager is null or not:" + (notificationManager == null));
        // define notification builder
        mBuilder = new NotificationCompat.Builder(context, context.getResources().getString(R.string.channel_id))
                .setSmallIcon(R.drawable.launch_logo)
                .setContentTitle("Quit Together")
                .setContentText("Your parter has made a quit plan. Please go to app and check.")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true);
        // notificationId is a unique int for each notification that you must define
        Log.d("QuitSmokeDebug", "mbuilder in onReceive is null or not:" + (mBuilder == null));
        notificationManager.notify(0, mBuilder.build());
    }
}
