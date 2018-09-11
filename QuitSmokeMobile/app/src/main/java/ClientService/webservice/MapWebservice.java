package clientservice.webservice;

import android.util.Log;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import clientservice.QuitSmokeClientUtils;

public class MapWebservice {
    // Original data retrieve from weather service
    private static JSONObject mapInfoByWS = null;

//    // get latitude and usage for all residents
//    public static ResidentMapEntity getLatLngAndUsageByAddress(List<SmartERUserWebservice.UserProfile> users, List<JSONObject> usageDataJsons, String viewType) throws IOException, JSONException {
//        // start to construct latLng map of all residents
//        Map<Integer, LatLng> LatLngProviders = new LinkedHashMap<>();
//        // encode address and generate ws request URL
//        StringBuilder urlBuilder = new StringBuilder(Constant.MAP_WS_MULTIPLE_LOCATION_URL);
//        for (SmartERUserWebservice.UserProfile userProfile : users){
//            userProfile.setAddress(userProfile.getAddress().replaceAll(" ", "%20").replaceAll("/", "%2F"));
//            urlBuilder.append(Constant.MAP_WS_LOCATION_URL_PARAM);
//            urlBuilder.append(userProfile.getAddress());
//            urlBuilder.append(Constant.MAP_WS_POSTCODE_URL_PARAM);
//            urlBuilder.append(userProfile.getPostCode());
//        }
//        // Call the service to get data
//        JSONObject jsonObject = webservice.requestWebService(urlBuilder.toString());
//        // get location result JSONArray
//        JSONArray jsonArray = jsonObject.getJSONArray(Constant.WS_KEY_MAP_RESULT);
//        // get latLng json from the ws result
//        int position = 0;
//        while (position < jsonArray.length()) {
//            JSONObject jsonObj = jsonArray.getJSONObject(position);
//            LatLng latLng = new LatLng();
//            // get json object locations
//            JSONArray jsonLocation = jsonObj.getJSONArray(Constant.WS_KEY_MAP_LOCATION);
//            // get latlng json obj
//            JSONObject jsonLatLng = jsonLocation.getJSONObject(0).getJSONObject(Constant.WS_KEY_MAP_LATLNG);
//            // set latlng value
//            latLng.setLatitude(jsonLatLng.getDouble(Constant.WS_KEY__MAP_LAT));
//            latLng.setLongitude(jsonLatLng.getDouble(Constant.WS_KEY_MAP_LNG));
//            LatLngProviders.put(users.get(position).getResId(), latLng);
//            position++;
//        }
//
//        // start to construct resident info list
//        List<ResidentUsageInfoEntity> residentUsageInfoEntities = new ArrayList<>();
//        for (JSONObject jsonObj : usageDataJsons) {
//            // get resident id from json object
//            int resid = jsonObj.getInt(Constant.WS_KEY_RESID);
//            // get totalUsage from json object
//            double totalUsage = jsonObj.getDouble(Constant.WS_KEY_MAP_TOTAL_USAGE);
//            // declare hour
//            int hour = 0;
//            if (Constant.MAP_VIEW_DAILY.equals(viewType)) {
//                // set hour to -1 due to daily view
//                hour = -1;
//            } else if (Constant.MAP_VIEW_HOURLY.equals(viewType)){
//                // get hour from json
//                hour = jsonObj.getInt(Constant.WS_KEY_MAP_TIME);
//            }
//            // construct resident info entity
//            ResidentUsageInfoEntity resEntity = new ResidentUsageInfoEntity(resid, hour, totalUsage);
//            // add it into list
//            residentUsageInfoEntities.add(resEntity);
//        }
//
//        // construct return result
//        ResidentMapEntity result = new ResidentMapEntity(LatLngProviders, residentUsageInfoEntities);
//        return result;
//    }

//    // get latitude by address
//    public static LatLng getLatLngByAddress(String address) {
//        LatLng result = new LatLng();
//        // country
//        String country = "";
//
//        try {
//            // encode address and generate ws request URL
//            StringBuilder urlBuilder = new StringBuilder(Constant.MAP_WS_URL);
//            urlBuilder.append(address.replaceAll(" ", "%20"));
//            // Call the service to get data
//            JSONObject jsonObject = webservice.requestWebService(urlBuilder.toString());
//            // get results json
//            JSONObject jsonResult = jsonObject.getJSONArray(Constant.WS_KEY_MAP_RESULT).getJSONObject(0);
//            // get location json
//            JSONObject jsonLocation = jsonResult.getJSONArray(Constant.WS_KEY_MAP_LOCATION).getJSONObject(0);
//            // get latlng
//            JSONObject jsonLatlng = jsonLocation.getJSONObject(Constant.WS_KEY_MAP_LATLNG);
//            // get country
//            country = jsonLocation.getString(Constant.WS_KEY_MAP_COUNTRY);
//            // set country to application level variable
//            SmartERMobileUtility.setCountry(country);
//
//            if (jsonLatlng != null && jsonLatlng.length() > 0) {
//                result.setLatitude(jsonLatlng.getDouble(Constant.WS_KEY__MAP_LAT));
//                result.setLongitude(jsonLatlng.getDouble(Constant.WS_KEY_MAP_LNG));
//            }
//        } catch (Exception ex) {
//            Log.e("SmartERDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
//        }
//        return result;
//    }
//
    // the map entity result class used to transfer latLng info and user usage info for all residents
    public static class ResidentMapEntity {
        // list of locations of all residents
        private Map<Integer, LatLng> latLngMap;
        // list of usage info for all residents

        // constructors
        public ResidentMapEntity() {}

        public ResidentMapEntity(Map<Integer, LatLng> latLngMap) {
            this.latLngMap = latLngMap;
        }

        // getters
        public Map<Integer, LatLng> getLatLngMap() {
            return latLngMap;
        }
    }

}
