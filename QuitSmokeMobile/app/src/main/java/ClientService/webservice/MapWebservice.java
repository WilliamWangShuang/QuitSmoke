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

import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.NoSmokeItem;
import clientservice.entities.NoSmokePlace;

public class MapWebservice {
    public static List<NoSmokePlace>getAllNoSmokePlaces() throws IOException, JSONException {
        List<NoSmokePlace> result = new ArrayList<>();

        // uri
        String url = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.MAP_GET_ALL_NO_SMOKE_PLACE;
        Log.d("QuitSmokeDebug", "url:" + url);
        // get json array from ws
        JSONArray jsonArray = BaseWebservice.requestWebServiceArray(url);

        // format json and construct return list
        if (jsonArray != null && jsonArray.length() > 0) {
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                NoSmokePlace noSmokePlace = new NoSmokePlace();
                // get place type
                noSmokePlace.setType((String)json.getString(QuitSmokeClientConstant.WS_MAP_TYPE));
                // get places of this type
                List<NoSmokeItem> placeItemList = new ArrayList<>();
                for (int j = 0; j < json.getJSONArray(QuitSmokeClientConstant.WS_MAP_NO_SMOKE_LIST).length(); j++) {
                    JSONObject jsonItem = json.getJSONArray(QuitSmokeClientConstant.WS_MAP_NO_SMOKE_LIST).getJSONObject(j);
                    NoSmokeItem noSmokeItem = new NoSmokeItem(
                            jsonItem.getString(QuitSmokeClientConstant.WS_MAP_ADDRESS),
                            jsonItem.getDouble(QuitSmokeClientConstant.WS_MAP_LATITUDE),
                            jsonItem.getDouble(QuitSmokeClientConstant.WS_MAP_LONGTITUDE),
                            jsonItem.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_NAME),
                            jsonItem.getString(QuitSmokeClientConstant.WS_MAP_TYPE));
                    placeItemList.add(noSmokeItem);
                }
                // add place item list to place entity
                noSmokePlace.setList(placeItemList);
                // add this no smoke place entity to result
                result.add(noSmokePlace);
            }
        }
        return result;
    }

}
