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

import com.example.william.quitsmokeappclient.R;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapquest.mapping.maps.MapView;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.OnMapReadyCallback;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import clientservice.GPSTracker;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.MapWebservice;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragmentFactorial extends AsyncTask<Void, Void, MapWebservice.ResidentMapEntity> {
    Bundle savedInstanceState = null;
    Context mContext;
    MapView mMapView = null;
    LatLng myLocation = null;
    MapWebservice.ResidentMapEntity residentInfo = null;
    MapboxMap mMapboxMap = null;
    //    List<SmartERUserWebservice.UserProfile> users = null;
    List<JSONObject> dataJson = null;
    String viewType = "daily";
    GPSTracker mGPS;
    private double latitude;
    private double longtitude;
    private LocationManager mLocationManager;

    // constructor
    public MapFragmentFactorial(MapView mMapView, Bundle savedInstanceState, Context mContext, String viewType) {
        this.savedInstanceState = savedInstanceState;
        this.mContext = mContext;
        this.mMapView = mMapView;
        this.viewType = viewType;
        mGPS = new GPSTracker(mContext);
        latitude = mGPS.getLocation().getLatitude();
        longtitude = mGPS.getLocation().getLongitude();
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if (mGPS.canGetLocation()) {
                latitude = mGPS.getLocation().getLatitude();
                longtitude = mGPS.getLocation().getLongitude();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MapWebservice.ResidentMapEntity doInBackground(Void... params) {
        Log.d("SmartERDebug", "****Set map****");

        MapWebservice.ResidentMapEntity result = null;

        try {
            // call ws to get all users
//            users = SmartERUserWebservice.findAllUsers();
            // get default view usage json data
            // TODO: should be the date before current date, here for demo purpose, set 2018-3-3
            //Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.HOUR_OF_DAY, -24);
            //Date date = cal.getTime();
            Calendar cal = new GregorianCalendar(2018, 2, 3);
            Date date = cal.getTime();
//            dataJson = SmartERUsageWebservice.getDailyTotalUsageOrHourlyUsagesForAllResident(viewType, date);
//            Log.d("SmartERDebug", "dataJson size:" + dataJson.size());
            // call ws to generate all Latlng and usage(hourly / daily) info for all users.
//            result = MapWebservice.getLatLngAndUsageByAddress(users, dataJson, viewType);
        } catch (Exception e) {
            Log.e("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(e));
        }

        return result;
    }

    @Override
    protected void onPostExecute(MapWebservice.ResidentMapEntity result) {
        mLocationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Please grant location access in your device settings for this app.", Toast.LENGTH_LONG).show();
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, mLocationListener);
        myLocation = new LatLng();
        Log.d("QuitSmokeDebug", "my position - " + "lat:" + latitude + ",long:" + longtitude);
        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longtitude);

        residentInfo = result;
        // synchronize map view
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                Log.d("SmartERDebug","my location:" + myLocation.getLatitude() + " : " + myLocation.getLongitude());
                mMapboxMap = mapboxMap;
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                // remove all markers first
                List<Marker> allMarkers = mMapboxMap.getMarkers();
                for (Marker marker : allMarkers){
                    mMapboxMap.removeMarker(marker);
                }
                // add makers for all residents
//                addMarker(mMapboxMap, residentInfo, viewType);
            }
        });
    }

    // add maker on map
    private void addMarker(MapboxMap mapboxMap, MapWebservice.ResidentMapEntity resInfo, String viewType) {
        // Create an Icon object for the marker to use
        IconFactory iconFactory = IconFactory.getInstance(mContext);
        Icon iconGreen = iconFactory.fromResource(R.drawable.marker_green);
        Icon iconRed = iconFactory.fromResource(R.drawable.marker_red);
        // set makers for all resident
        for (Map.Entry<Integer, LatLng> entity : resInfo.getLatLngMap().entrySet()) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(entity.getValue());
            // if resident obj is null. it means for this resident, no usage data found matching the given time
            double totalUsage = 0;
            markerOptions.title("");
            // set marker color based on view type and usage
            if("daily".equals(viewType)) {
                markerOptions.snippet("Daily Total Usage:" + (totalUsage == 0 ? "N.A." : totalUsage));
                markerOptions.setIcon(totalUsage > 21 ? iconRed : iconGreen);
            } else {
                markerOptions.snippet("Daily Total Usage:" + (totalUsage == 0 ? "N.A." : totalUsage));
                markerOptions.setIcon(totalUsage > 21 ? iconRed : iconGreen);
            }
            mapboxMap.addMarker(markerOptions);
        }

    }
}
