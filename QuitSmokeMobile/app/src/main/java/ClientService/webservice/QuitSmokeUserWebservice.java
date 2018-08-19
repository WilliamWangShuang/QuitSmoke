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
    // get resident by username
    public static boolean checkUserExistByEmail(String email) throws IOException, JSONException {
        // result
        boolean result = true;
        try {
            result = Boolean.parseBoolean(BaseWebservice.requestWebServicePlainText(QuitSmokeClientConstant.WEB_SERVER_BASE_URI
                    + QuitSmokeClientConstant.FIND_USER_BY_EMAIL_WS
                    + email));
        } catch (NullPointerException ex) {
            // if null pointer exception, means no use exist in db with same email, then return null
            return true;
        } catch (Exception ex) {
            throw ex;
        }
        // return result
        return result;
    }

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
//    // find resident by username and passwords
//    public static UserProfile findUserByUsernameAndPwd(String username, String pwd) throws IOException, JSONException, ParseException {
//        UserProfile result = null;
//        JSONObject credentialJson = null;
//
//        try {
//            // get json array from ws
//            JSONArray jsonArray = webservice.requestWebServiceArray(Constant.FIND_USER_CREDENTIAL + username + "/" + pwd);
//            Log.d("SmartERDebug", "json array length:" + jsonArray.length());
//            // get use credential by username and pwd
//            if (jsonArray.length() > 0)
//                credentialJson = jsonArray.getJSONObject(0);
//            else
//                credentialJson = null;
//
//            if (credentialJson != null) {
//                // get resident json object
//                JSONObject residentJsonObj = credentialJson.getJSONObject(Constant.WS_KEY_RESID);
//                result = new UserProfile(residentJsonObj);
//            }
//        } catch (Exception ex) {
//            throw ex;
//        }
//        return result;
//    }

    // post to server to store register user
    public static boolean saveRegisterResident(UserInfoEntity registerInfoUI) throws JSONException, IOException, ParseException {
        boolean isSuccessSave = false;
        // construct resident json
        JSONObject jsonResident = new JSONObject();
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_CITY, registerInfoUI.getCity());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_EMAIL, registerInfoUI.getEmail());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_NAME, registerInfoUI.getName());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_PARTNER_ID, registerInfoUI.getPartnerId());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_PARTNER_I, registerInfoUI.isPartner());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_SMOKER_I, registerInfoUI.isSmoker());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_PASSWORD, registerInfoUI.getPassword());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_PLAN_ID, QuitSmokeClientUtils.isNullOrEmpty(registerInfoUI.getPlanId()) ? -1 : Integer.parseInt(registerInfoUI.getPlanId()));
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_POINT, registerInfoUI.getPoint());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_REGISTER_DT, registerInfoUI.getRegisterDate());
        jsonResident.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_SUBURB, registerInfoUI.getSuburb());
        Log.d("SmartERDebug", "parsed resident json to post:" + jsonResident.toString());
        // call ws to save
        String saveResidentResult = BaseWebservice.postWebService(QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.REGISTER_WS, jsonResident);
        // if save resident successfully, start save credential
        if (QuitSmokeClientConstant.SUCCESS_MSG.equals(saveResidentResult)) {
            isSuccessSave = true;
        }

        return isSuccessSave;
    }

}
