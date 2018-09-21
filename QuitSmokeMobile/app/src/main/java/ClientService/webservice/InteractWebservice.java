package clientservice.webservice;

import android.util.Log;
import clientservice.entities.CalculateFrsEntity;
import clientservice.entities.PlanEntity;
import clientservice.entities.SurveyResultEntity;
import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
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
        String uri = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.CREATE_PLAN_WS;
        // construct json for request
        JSONObject json = new JSONObject();
        json.put(QuitSmokeClientConstant.WS_INTERACT_PLAN_TARGET_AMOUNT, targetAmount);
        json.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_UID, QuitSmokeClientUtils.getUid());
        json.put(QuitSmokeClientConstant.WS_INTERACT_PLAN_CREATE_DT, QuitSmokeClientUtils.convertDateToString(new Date()));
        json.put(QuitSmokeClientConstant.WS_INTERACT_PLAN_STATUS, QuitSmokeClientConstant.STATUS_PENDING);
        json.put(QuitSmokeClientConstant.WS_INTERACT_PLAN_REAL_AMOUNT, 0);
        Log.d("QuitSmokeDebug", "parsed json to post:" + json.toString());

        // call ws on server side to get calculation result
        newPlanNodeName = BaseWebservice.postWSForGetRestrievePlainText(uri, json);
        Log.d("QuitSmokeDebug", "ws result from server:" + newPlanNodeName);
        return newPlanNodeName;
    }

    // get pending plan based on partner email
    public static ArrayList<PlanEntity> getPendingPlanByPartnerEmail(String email) throws JSONException, IOException{
        ArrayList<PlanEntity> resultList = new ArrayList<>();
        JSONObject reqJson = new JSONObject();
        reqJson.put(QuitSmokeClientConstant.WS_INTERACT_QUERY_PLAN_EMAIL, email);
        JSONArray jsonArray = BaseWebservice.postWebServiceForGetRestrieveJSONArray(QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.GET_PENDING_PLAN, reqJson);

        // if ws result not null, construct domain returned object
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                PlanEntity result = new PlanEntity();
                result.setCreateDate(json.getString(QuitSmokeClientConstant.WS_INTERACT_PLAN_CREATE_DT));
                result.setStatus(json.getString(QuitSmokeClientConstant.WS_INTERACT_PLAN_STATUS));
                result.setTargetAmount(json.getInt(QuitSmokeClientConstant.WS_INTERACT_PLAN_TARGET_AMOUNT));
                result.setRealAmount(json.getInt(QuitSmokeClientConstant.WS_INTERACT_PLAN_REAL_AMOUNT));
                result.setUid(json.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_UID));
                resultList.add(result);
            }
        }

        return resultList;
    }

    public static boolean approvePlan(String uid, int amount) throws JSONException, IOException {
        boolean result = false;
        // url
        String url = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.APPROVE_PLAN;
        // construct request json
        JSONObject reqJson = new JSONObject();
        reqJson.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_UID, uid);
        reqJson.put(QuitSmokeClientConstant.WS_INTERACT_APPROVE_PLAN_AMOUNT, amount);

        // call ws to get response result
        result = Boolean.parseBoolean(BaseWebservice.postWSForGetRestrievePlainText(url, reqJson));

        return result;
    }

    public static PlanEntity getProceedingPlan(String uid) throws JSONException, IOException {
        PlanEntity result = null;
        String url = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.GET_CURRENT_PLAN;
        // construct request json
        JSONObject reqJson = new JSONObject();
        reqJson.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_UID, uid);

        // call ws to get response result
        JSONObject json = BaseWebservice.postWebServiceForGetRestrieveJSON(url, reqJson);
        Log.d("QuitSmokeDebug","Get current plan result from backend ws is null:" + (json == null));
        // construct result plan object
        if (json != null) {
            result = new PlanEntity();
            result.setUid(uid);
            result.setTargetAmount(json.getInt(QuitSmokeClientConstant.WS_INTERACT_PLAN_TARGET_AMOUNT));
            result.setRealAmount(json.getInt(QuitSmokeClientConstant.WS_INTERACT_PLAN_REAL_AMOUNT));
            result.setStatus(json.getString(QuitSmokeClientConstant.WS_INTERACT_PLAN_STATUS));
            result.setCreateDate(json.getString(QuitSmokeClientConstant.WS_INTERACT_PLAN_CREATE_DT));
            result.setEncouragement(json.getString(QuitSmokeClientConstant.WS_INTERACT_ENCOURAGEMENT_ENCOURAGEMENT));
        }
        return result;
    }

    public static boolean addRealAmount(String uid) throws JSONException, IOException {
        boolean result = false;
        String url = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.ADD_REAL_AMOUNT;

        // call ws to get response result
        String json = BaseWebservice.postWSForGetRestrievePlainText(url, uid);
        Log.d("QuitSmokeDebug","add real amount result from backend ws:" + json);
        result = Boolean.parseBoolean(json);

        return result;
    }

    public static boolean updateEncouragement(String smokerUid, String newEncouragement) throws JSONException, IOException {
        boolean result = false;
        Log.d("QuitSmokeDebug", "updateEncouragement starts.");
        String url = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.UPDATE_ENCOURAGEMENT;
        Log.d("QuitSmokeDebug", "update encouragement url:" + url);

        // construct request json
        JSONObject jsonReq = new JSONObject();
        jsonReq.put(QuitSmokeClientConstant.WS_INTERACT_ENCOURAGEMENT_SMOKER_UID, smokerUid);
        jsonReq.put(QuitSmokeClientConstant.WS_INTERACT_ENCOURAGEMENT_ENCOURAGE, newEncouragement);
        Log.d("QuitSmokeDebug", "update encouragement json:" + jsonReq.toString());
        // call ws to get update encouragement
        String response = BaseWebservice.postWSForGetRestrievePlainText(url, jsonReq);
        result = Boolean.parseBoolean(response);

        Log.d("QuitSmokeDebug", "updateEncouragement result:" + result);
        return result;
    }
}
