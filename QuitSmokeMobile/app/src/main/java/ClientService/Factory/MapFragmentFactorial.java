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

import com.example.william.quitsmokeappclient.MainActivity;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import clientservice.GPSTracker;
import clientservice.QuitSmokeClientUtils;
import clientservice.db.QuitSmokeDbUtility;
import clientservice.entities.NoSmokeItem;
import clientservice.entities.NoSmokePlace;
import clientservice.webservice.MapWebservice;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragmentFactorial extends AsyncTask<Void, Void, Void> {
    Bundle savedInstanceState;
    Context mContext;
    MapView mMapView;
    LatLng myLocation = null;
    MapboxMap mMapboxMap = null;
    GPSTracker mGPS;
    private double latitude;
    private double longtitude;
    private LocationManager mLocationManager;
    private List<NoSmokePlace> noSmokePlaceList;
    private String viewType;
    private final IconFactory iconFactory = IconFactory.getInstance(mContext);
    private final Icon iconGreen = iconFactory.fromResource(R.drawable.marker_green);
    private  final Icon iconRed = iconFactory.fromResource(R.drawable.marker_red);

    // constructor
    public MapFragmentFactorial(MapView mMapView, Bundle savedInstanceState, Context mContext, String viewType) {
        this.savedInstanceState = savedInstanceState;
        this.mContext = mContext;
        this.mMapView = mMapView;
        mGPS = new GPSTracker(mContext);
        latitude = mGPS.getLocation().getLatitude();
        longtitude = mGPS.getLocation().getLongitude();
        this.viewType = viewType;
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
    protected Void doInBackground(Void... params) {
        Log.d("QuitSmokeDebug", "****Set map****");
        try {
            // call webservice to get all no smoke places
            QuitSmokeDbUtility quitSmokeDbUtility = new QuitSmokeDbUtility(mContext);
            noSmokePlaceList = quitSmokeDbUtility.getAllExistingPlaces();
            Log.d("QuitSmokeDebug", "noSmokePlaceList length:" + noSmokePlaceList.size());

        } catch (Exception e) {
            Log.e("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(e));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
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
//        myLocation.setLatitude(-37.8169724245);
//        myLocation.setLongitude(144.96413018);

        // synchronize map view
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                Log.d("QuitSmokeDebug","my location:" + myLocation.getLatitude() + " : " + myLocation.getLongitude());
                mMapboxMap = mapboxMap;
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
                // remove all markers first
                List<Marker> allMarkers = mMapboxMap.getMarkers();
                for (Marker marker : allMarkers){
                    mMapboxMap.removeMarker(marker);
                }
                // set maker for current user position
                setMarker(mMapboxMap, iconGreen, "I am here", new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
                // add makers for all residents
                addMarker(mMapboxMap, viewType);
            }
        });
    }

    // add maker on map
    private void addMarker(MapboxMap mapboxMap, String viewType) {
        // get LatLng list from no smoke place list
        for(NoSmokePlace noSmokePlace : noSmokePlaceList) {
            // get current place type
            String noSmokePlaceType = noSmokePlace.getType();
            // check place type, if same as selected, add all its place items' lat & long
            if (viewType.equals(noSmokePlaceType)) {
                // add latitude and longtitude of all this type of places
                boolean isListNull = noSmokePlace.getList() == null;
                Log.d("TestDebug", "if get list is null" + isListNull);
                if (!isListNull) {
                    for (NoSmokeItem item : noSmokePlace.getList()) {
                        double lat = item.getLatitude();
                        double lon = item.getLongitude();
                        Log.d("QuitSmokeDebug", "latitude:" + lat + ",longitude:" + lon);
                        LatLng latLng = new LatLng();
                        latLng.setLatitude(lat);
                        latLng.setLongitude(lon);
                        // set marker
                        setMarker(mapboxMap, iconGreen, item.getName(), latLng);
                    }
                }
            }
        }
    }

    private void setMarker(MapboxMap mapboxMap, Icon iconGreen, String name, LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.snippet(name);
        markerOptions.position(latLng);
        markerOptions.setIcon(iconGreen);
        mapboxMap.addMarker(markerOptions);
    }
}
