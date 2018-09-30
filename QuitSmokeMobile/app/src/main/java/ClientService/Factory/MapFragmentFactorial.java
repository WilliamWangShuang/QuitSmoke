package clientservice.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.example.william.quitsmokeappclient.R;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapquest.mapping.listener.ZoomListener;
import com.mapquest.mapping.maps.MapView;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import clientservice.QuitSmokeClientUtils;
import clientservice.db.QuitSmokeDbUtility;
import clientservice.entities.NoSmokeItem;
import clientservice.entities.NoSmokePlace;

public class MapFragmentFactorial extends AsyncTask<Void, Void, Void> {
    Bundle savedInstanceState;
    Context mContext;
    MapView mMapView;
    MapboxMap mMapboxMap = null;
    private List<NoSmokePlace> noSmokePlaceList;
    private List<Marker> originalMarkers;
    private LatLng firstNodePos;
    private String viewType;
    private final IconFactory iconFactory = IconFactory.getInstance(mContext);
    private final Icon iconGreen = iconFactory.fromResource(R.drawable.marker_green);
    private final Icon iconBakery = iconFactory.fromResource(R.drawable.bakery);
    private final Icon iconSport = iconFactory.fromResource(R.drawable.gym);
    private final Icon iconPark = iconFactory.fromResource(R.drawable.park);


    // constructor
    public MapFragmentFactorial(MapView mMapView, Bundle savedInstanceState, Context mContext, String viewType) {
        this.savedInstanceState = savedInstanceState;
        this.mContext = mContext;
        this.mMapView = mMapView;
        this.viewType = viewType;
    }

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
        // synchronize map view
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                // remove all markers first
                removeMarkers();
                mMapboxMap.addZoomListener(new ZoomListener() {
                    @Override
                    public void onZoomChange(double oldZoom, double newZoom) {
                        adjustMarkser(mMapboxMap, newZoom);
                    }
                }, 0);
                // add makers for all residents
                addMarker(mMapboxMap, viewType);
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstNodePos, 13));
            }
        });
    }

    private void adjustMarkser(MapboxMap mapboxMap,double newZoom) {
        // set original marker list
//        originalMarkers = mapboxMap.getMarkers();
//        // recover all markers, then calculate which ones to remove
//        double ration = Math.round(newZoom / 17 * 100.00) / 100.00;
//        int hidSize = (int)(originalMarkers.size() * ration);
//        for (int i = 0; i <= hidSize; i++) {
//            originalMarkers.get(i).setIcon(iconFactory.fromBitmap(Bitmap.createBitmap(1,1,null)));
//        }
//        mMapView.
    }

    private void removeMarkers() {
        List<Marker> allMarkers = mMapboxMap.getMarkers();
        for (Marker marker : allMarkers){
            mMapboxMap.removeMarker(marker);
        }
    }

    // add maker on map
    private void addMarker(MapboxMap mapboxMap, String viewType) {
        // get LatLng list from no smoke place list
        for(NoSmokePlace noSmokePlace : noSmokePlaceList) {
            // get current place type
            String noSmokePlaceType = noSmokePlace.getType();
            // check place type, if same as selected, add all its place items' lat & long
            if (viewType.equals(noSmokePlaceType)) {
                // add latitude and longitude of all this type of places
                boolean isListNull = noSmokePlace.getList() == null;
                // get icon based on type
                Icon icon = getIconBasedOnType(viewType);
                if (!isListNull) {
                    boolean isFirstPlace = true;
                    for (NoSmokeItem item : noSmokePlace.getList()) {
                        double lat = item.getLatitude();
                        double lon = item.getLongitude();
                        if (isFirstPlace) {
                            firstNodePos = new LatLng(lat, lon);
                            isFirstPlace = false;
                        }
                        LatLng latLng = new LatLng();
                        latLng.setLatitude(lat);
                        latLng.setLongitude(lon);
                        // set icon based on place type
                        // set marker
                        setMarker(mapboxMap, icon, item.getName(), latLng);
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

    // this method is seriously big bug. Because it is hardcode. Need to refine to make it auto compatible to db data (place_type)
    private Icon getIconBasedOnType(String viewType) {
        Icon icon = null;
        if ("Bakery Product Manufacturing (Non-factory based)".equals(viewType))
            icon = iconBakery;
        else if ("Sports".equals(viewType))
            icon = iconSport;
        else if ("Park".equals(viewType))
            icon = iconPark;
        else if ("Cafes and Restaurants".equals(viewType))
            icon = iconGreen;

        return icon;
    }

}
