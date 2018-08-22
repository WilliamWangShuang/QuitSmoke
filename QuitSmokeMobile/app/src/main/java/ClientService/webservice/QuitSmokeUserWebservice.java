package ClientService.webservice;

import android.util.Log;

import ClientService.Entities.UserInfoEntity;
import ClientService.QuitSmokeClientConstant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import ClientService.Factory.RegisterFactorial;
import ClientService.QuitSmokeClientUtils;

public class QuitSmokeUserWebservice {
    //    // get profile of all users
//    public static List<UserProfile> findAllUsers() throws IOException, JSONException, ParseException {
//        List<UserProfile> users = new ArrayList<>();
//
//        // ws query result object
//        JSONArray jsonArray = new JSONArray();
//        try {
//            jsonArray = webservice.requestWebServiceArray(Constant.FIND_ALL_USERS);
//
//            // construct return list
//            int position = 0;
//            while (position < jsonArray.length()) {
//                JSONObject jsonObj = jsonArray.getJSONObject(position);
//                UserProfile userProfile = new UserProfile(jsonObj);
//                users.add(userProfile);
//                position ++;
//            }
//        } catch (Exception ex) {
//            throw ex;
//        }
//
//        //return result
//        return users;
//    }
//
    // find resident by username and passwords
    public static UserInfoEntity findUserByEmailAndPwd(String email, String pwd) throws IOException, JSONException, ParseException {
        UserInfoEntity result = null;
        JSONObject appUserJson = null;

        try {
            // construct user json
            JSONObject jsonResident = new JSONObject();
            jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_EMAIL, email);
            jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_PASSWORD, pwd);
            Log.d("QuitSmokeDebug", "parsed resident json to post:" + jsonResident.toString());
            // get json array from ws
            JSONObject jsonObj = BaseWebservice.postWebServiceForGetRestrieveJSON(QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.LOGIN_URI_WS, jsonResident);
            Log.d("QuitSmokeDebug", "json array length:" + jsonObj.length());
            // get use credential by username and pwd
            if (jsonObj.length() > 0) {
                appUserJson = jsonObj;
                // construct json to result object
                result = new UserInfoEntity();
                result.setName(appUserJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_NAME));
                result.setSmoker(appUserJson.getBoolean(QuitSmokeClientConstant.WS_JSON_USER_KEY_SMOKER_I));
                result.setPartner(appUserJson.getBoolean(QuitSmokeClientConstant.WS_JSON_USER_KEY_PARTNER_I));
                result.setRegisterDate(appUserJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_REGISTER_DT));
                result.setSuburb(appUserJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_SUBURB));
                result.setCity(appUserJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_CITY));
                result.setPoint(appUserJson.getInt(QuitSmokeClientConstant.WS_JSON_USER_KEY_POINT));
                result.setPartnerId(appUserJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_PARTNER_ID));
                result.setPlanId(appUserJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_PLAN_ID));
                result.setUid(appUserJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_UID));
            }
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    // post to server to store register user
    public static boolean saveRegisterResident(UserInfoEntity registerInfoUI) throws JSONException, IOException, ParseException {
        boolean isSuccessSave = false;
        // construct user json
        JSONObject jsonResident = new JSONObject();
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_CITY, registerInfoUI.getCity());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_EMAIL, registerInfoUI.getEmail());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_NAME, registerInfoUI.getName());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_PARTNER_ID, registerInfoUI.getPartnerId());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_PARTNER_I, registerInfoUI.isPartner());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_SMOKER_I, registerInfoUI.isSmoker());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_PASSWORD, QuitSmokeClientUtils.encryptPwd(registerInfoUI.getPassword()));
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_POINT, registerInfoUI.getPoint());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_REGISTER_DT, QuitSmokeClientUtils.convertDateToString(new Date()));
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_SUBURB, registerInfoUI.getSuburb());
        Log.d("QuitSmokeDebug", "parsed resident json to post:" + jsonResident.toString());
        // call ws to save
        String saveResidentResult = BaseWebservice.postWebService(QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.REGISTER_WS, jsonResident);
        Log.d("QuitSmokeDebug", "result from backend:" + saveResidentResult);
        // if save resident successfully, start save credential
        if (QuitSmokeClientConstant.SUCCESS_MSG.equals(saveResidentResult)) {
            isSuccessSave = true;
        } else if (QuitSmokeClientConstant.EMAIL_EXIST.equals(saveResidentResult)) {
            isSuccessSave = false;
        }

        return isSuccessSave;
    }

}
