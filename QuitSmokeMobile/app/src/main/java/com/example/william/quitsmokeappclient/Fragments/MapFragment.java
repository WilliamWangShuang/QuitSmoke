package com.example.william.quitsmokeappclient.Fragments;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.mapquest.mapping.maps.MapView;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.example.william.quitsmokeappclient.R;

import clientservice.factory.MapFragmentFactorial;

public class MapFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View vMapFragment;
    private MapView mMapView;
    // my address geographical position
    private LatLng myLocation;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMapFragment = inflater.inflate(R.layout.map_fragment, container, false);
        // create map view
        mMapView = (MapView)vMapFragment.findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);
        // register listener to spinner
        ((Spinner)vMapFragment.findViewById(R.id.spnnierMapViewType)).setOnItemSelectedListener(this);
        return vMapFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // start map view asynchronically
        MapFragmentFactorial mapFragmentFactorial = new MapFragmentFactorial(mMapView, savedInstanceState, getContext(),"daily");
        mapFragmentFactorial.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
        // get view type from view
        String viewType = adapter.getItemAtPosition(position).toString();
        MapFragmentFactorial mapFragmentFactorial = null;
        mapFragmentFactorial = new MapFragmentFactorial(mMapView, null, getContext(), "daily");
        // change map view according to the view type selected
//        if (Constant.MAP_VIEW_HOURLY.equals(viewType)) {
//            mapFragmentFactorial = new MapFragmentFactorial(mMapView, null, Constant.MAP_VIEW_HOURLY);
//        } else if(Constant.MAP_VIEW_DAILY.equals(viewType)) {
//            mapFragmentFactorial = new MapFragmentFactorial(mMapView, null, Constant.MAP_VIEW_DAILY);
//        } else {
//            mapFragmentFactorial = new MapFragmentFactorial(mMapView, null, Constant.MAP_VIEW_DAILY);
//        }
        mapFragmentFactorial.execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.d("SmartERDebug", "on save instance state...");
        super.onSaveInstanceState(outState);
    }
}
