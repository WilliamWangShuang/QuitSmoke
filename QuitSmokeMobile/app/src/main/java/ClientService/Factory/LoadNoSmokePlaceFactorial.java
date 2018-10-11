package clientservice.factory;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.quitsmoke.william.quitsmokeappclient.Interface.ILoadNoSmokePlaceAsyncResponse;
import com.quitsmoke.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import com.quitsmoke.william.quitsmokeappclient.R;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapquest.mapping.maps.MapView;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.OnMapReadyCallback;

import java.util.List;

import clientservice.GPSTracker;
import clientservice.QuitSmokeClientUtils;
import clientservice.db.QuitSmokeDbUtility;
import clientservice.entities.NoSmokeItem;
import clientservice.entities.NoSmokePlace;
import clientservice.webservice.MapWebservice;

import static android.content.Context.LOCATION_SERVICE;

public class LoadNoSmokePlaceFactorial extends AsyncTask<Void, Void, List<NoSmokePlace>> {
    Context mContext;
    private List<NoSmokePlace> noSmokePlaceList;
    private QuitSmokeDbUtility quitSmokeDbUtility;
    public ILoadNoSmokePlaceAsyncResponse delegate = null;

    // constructor
    public LoadNoSmokePlaceFactorial(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<NoSmokePlace> doInBackground(Void... params) {
        Log.d("QuitSmokeDebug", "****Load no smoke place map****");
        try {
            // call webservice to get all no smoke places
            // initial SQLite
            quitSmokeDbUtility = new QuitSmokeDbUtility(mContext);
            quitSmokeDbUtility.truncateNoSmokePlaceTable();
            noSmokePlaceList = MapWebservice.getAllNoSmokePlaces();
            Log.d("QuitSmokeDebug", "noSmokePlaceList length:" + noSmokePlaceList.size());

        } catch (Exception e) {
            Log.e("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(e));
        }

        return noSmokePlaceList;
    }

    @Override
    protected void onPostExecute(List<NoSmokePlace> noSmokePlaces) {
        Log.d("QuitSmokeDebug", "****Load no smoke place map end.****");
        for (NoSmokePlace place : noSmokePlaceList) {
            for (NoSmokeItem item : place.getList()) {
                quitSmokeDbUtility.insertNoSmokePlaceUsage(item.getAddress(), item.getLatitude(), item.getLongitude(), item.getName(), item.getType());
            }
        }
        delegate.processFinish(noSmokePlaces);
    }
}
