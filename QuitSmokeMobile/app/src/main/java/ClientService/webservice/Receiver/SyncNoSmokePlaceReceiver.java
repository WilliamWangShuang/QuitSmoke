package clientservice.webservice.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.william.quitsmokeappclient.Interface.ILoadNoSmokePlaceAsyncResponse;
import java.util.List;
import clientservice.QuitSmokeClientUtils;
import clientservice.db.QuitSmokeDbUtility;
import clientservice.entities.NoSmokePlace;
import clientservice.factory.LoadNoSmokePlaceFactorial;

public class SyncNoSmokePlaceReceiver extends BroadcastReceiver implements ILoadNoSmokePlaceAsyncResponse {
    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;
    private Context context;
    private QuitSmokeDbUtility quitSmokeDbUtility;
    private Context mContext;
    private List<NoSmokePlace> noSmokePlaceList;
    private LoadNoSmokePlaceFactorial loadNoSmokePlaceFactorial;

    public SyncNoSmokePlaceReceiver() {}

    public SyncNoSmokePlaceReceiver(Context context){
        Log.d("QuitSmokeDebug", "start constructor of SyncNoSmokePlaceReceiver");
        // get context
        this.context = context;
        // Initial alarm manager used to set repeat clock
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Define which intent to be broadcast to context
        i = new Intent(context, SyncNoSmokePlaceReceiver.class);
        // get current pending board cast intent in context
        pi = PendingIntent.getBroadcast(context,0, i,0);
        // Set alarm do the job of the intent
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("QuitSmokeDebug", "start sync SQLite data with DB for no smoke places");
        try {
            // get data from DB
            loadNoSmokePlaceFactorial = new LoadNoSmokePlaceFactorial(context);
            loadNoSmokePlaceFactorial.delegate = this;
            loadNoSmokePlaceFactorial.execute();
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
        }
    }

    @Override
    public void processFinish(List<NoSmokePlace> reponseResult) {
        // sync SQLite data
        noSmokePlaceList = reponseResult;

        Log.d("QuitSmokeDebug", "noSmokePlaceList length:" + noSmokePlaceList.size());
    }
}
