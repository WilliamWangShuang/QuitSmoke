package ClientService.webservice;

import android.util.Log;
import ClientService.Entities.CalculateFrsEntity;
import ClientService.Entities.SurveyResultEntity;
import ClientService.QuitSmokeClientConstant;
import ClientService.QuitSmokeClientUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Date;

public class InteractWebservice {

    // check if a user has set a supporter
    public static boolean isSupporterSet() throws IOException {
        boolean result = false;
        // format uri
        String uri = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.CHECK_PARTNER;

        // call ws on server side to get calculation result
        result = Boolean.parseBoolean(BaseWebservice.postWSForGetRestrievePlainText(uri, QuitSmokeClientUtils.getUid()));
        Log.d("QuitSmokeDebug", "ws result from server:" + result);
        return result;
    }

    // create plan
    public static String createPlan(int targetAmount) throws JSONException, IOException {
        String newPlanNodeName = "";
        // format uri
        String uri = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.CALCULATE_FRS;
        // construct json for request
        JSONObject json = new JSONObject();
        json.put(QuitSmokeClientConstant.WS_INTERACT_PLAN_TARGET_AMOUNT, targetAmount);
        json.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_UID, QuitSmokeClientUtils.getUid());
        json.put(QuitSmokeClientConstant.WS_INTERACT_PLAN_CREATE_DT, QuitSmokeClientUtils.convertDateToString(new Date()));
        json.put(QuitSmokeClientConstant.WS_INTERACT_PLAN_STATUS, QuitSmokeClientConstant.STATUS_OPEN);
        json.put(QuitSmokeClientConstant.WS_INTERACT_PLAN_REAL_AMOUNT, 0);
        Log.d("QuitSmokeDebug", "parsed json to post:" + json.toString());

        // call ws on server side to get calculation result
        newPlanNodeName = BaseWebservice.postWSForGetRestrievePlainText(uri, json);
        Log.d("QuitSmokeDebug", "ws result from server:" + newPlanNodeName);
        return newPlanNodeName;
    }
}
