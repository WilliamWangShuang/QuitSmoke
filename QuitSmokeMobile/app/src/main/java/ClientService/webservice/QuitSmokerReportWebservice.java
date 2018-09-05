package clientservice.webservice;

import android.util.Log;
import clientservice.entities.CalculateFrsEntity;
import clientservice.entities.SurveyResultEntity;
import clientservice.QuitSmokeClientConstant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class QuitSmokerReportWebservice {

    // calculate Framingham Risk Score
    public static String calculateFRS(CalculateFrsEntity calculateFrsEntity) throws JSONException, IOException {
        String result = "";
        // format uri
        String uri = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.CALCULATE_FRS;
        // construct json for request
        JSONObject json = new JSONObject();
        json.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_AGE, calculateFrsEntity.getAge());
        json.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_GENDER, calculateFrsEntity.getGender());
        json.put(QuitSmokeClientConstant.WS_CALCULATE_FRS_KEY_CHOL, calculateFrsEntity.getChol());
        json.put(QuitSmokeClientConstant.WS_CALCULATE_FRS_KEY_HDL, calculateFrsEntity.getHdl());
        json.put(QuitSmokeClientConstant.WS_CALCULATE_FRS_KEY_SBP, calculateFrsEntity.getSbp());
        json.put(QuitSmokeClientConstant.WS_CALCULATE_FRS_KEY_TREATED_I, calculateFrsEntity.getIsTreated());
        Log.d("QuitSmokeDebug", "parsed json to post:" + json.toString());

        // call ws on server side to get calculation result
        result = BaseWebservice.postWSForGetRestrievePlainText(uri, json);
        Log.d("QuitSmokeDebug", "ws result from server:" + result);
        return result;
    }

    // get survey result for public
    public static SurveyResultEntity getSurveyResult(int age, String gender, int smokeNo) throws JSONException, IOException {
        SurveyResultEntity result = new SurveyResultEntity();
        // format uri
        String uri = QuitSmokeClientConstant.WEB_SERVER_BASE_URI + QuitSmokeClientConstant.GET_SERVEY_DATA;
        // construct json for request
        JSONObject json = new JSONObject();
        json.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_AGE, age);
        json.put(QuitSmokeClientConstant.WS_JSON_USER_KEY_GENDER, gender);
        json.put(QuitSmokeClientConstant.WS_SURVEY_KEY_SMOKE_NO, smokeNo);
        Log.d("QuitSmokeDebug", "parse json to post:" + json.toString());

        // call ws on server side to get survey result
        JSONObject jsonObject = BaseWebservice.postWebServiceForGetRestrieveJSON(uri, json);
        Log.d("QuitSmokeDebug", "ws response json:" + jsonObject.toString());

        // prepare result domain object
        // my mean object
        JSONObject myMeanJson = jsonObject.getJSONObject(QuitSmokeClientConstant.WS_SURVEY_KEY_MY_MEAN_GROUP);
        int ageStart = myMeanJson.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_AGE_START);
        int ageEnd = myMeanJson.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_AGE_END);
        String myMeanGender = myMeanJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_GENDER);
        int meanConsume = myMeanJson.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_MEAN_CONSUME);
        result.setMyMean(ageEnd, ageStart, myMeanGender, meanConsume);

        // mean entity list
        JSONArray meanListJson = jsonObject.getJSONArray(QuitSmokeClientConstant.WS_SURVEY_KEY_MEAN_LIST);
        for (int i = 0; i < meanListJson.length(); i++) {
            JSONObject obj = meanListJson.getJSONObject(i);
            int meanListAgeStart = obj.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_AGE_START);
            int meanListAgeEnd = obj.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_AGE_END);
            String meanListGender = obj.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_GENDER);
            int meanListConsume = obj.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_MEAN_CONSUME);
            result.addMeanEntity(meanListAgeEnd, meanListAgeStart, meanListGender, meanListConsume);
        }

        // chance age list
        JSONArray chanceAgeListJson = jsonObject.getJSONArray(QuitSmokeClientConstant.WS_SURVEY_KEY_CHANCE);
        for (int i = 0; i < chanceAgeListJson.length(); i++) {
            JSONObject obj = chanceAgeListJson.getJSONObject(i);
            int chanceAgeListAgeStart = obj.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_AGE_START);
            int chanceAgeListAgeEnd = obj.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_AGE_END);
            double chanceAgeListProportion = obj.getDouble(QuitSmokeClientConstant.WS_SURVEY_KEY_PROPORTION);
            String chanceAgeListBehaviour = obj.getString(QuitSmokeClientConstant.WS_SURVEY_KEY_BEHAVIOUR);
            result.addChanceAgeEntity(chanceAgeListAgeEnd, chanceAgeListAgeStart, chanceAgeListBehaviour, chanceAgeListProportion);
        }

        // motivation age list
        JSONArray motivationAgeListJson = jsonObject.getJSONArray(QuitSmokeClientConstant.WS_SURVEY_KEY_MOTIVATION_AGE);
        for (int i = 0; i < motivationAgeListJson.length(); i++) {
            JSONObject obj = motivationAgeListJson.getJSONObject(i);
            int motivationAgeListAgeStart = obj.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_AGE_START);
            int motivationAgeListAgeEnd = obj.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_AGE_END);
            double motivationAgeListProportion = obj.getDouble(QuitSmokeClientConstant.WS_SURVEY_KEY_PROPORTION);
            String motivationAgeListBehaviour = obj.getString(QuitSmokeClientConstant.WS_SURVEY_KEY_BEHAVIOUR);
            result.addMotivationAgeEntity(motivationAgeListAgeEnd, motivationAgeListAgeStart, motivationAgeListBehaviour, motivationAgeListProportion);
        }

        // motivation gender list
        JSONArray motivationGenderListJson = jsonObject.getJSONArray(QuitSmokeClientConstant.WS_SURVEY_KEY_MOTIVATION_GENDER);
        for (int i = 0; i < motivationGenderListJson.length(); i++) {
            JSONObject obj = motivationGenderListJson.getJSONObject(i);
            int rank = obj.getInt(QuitSmokeClientConstant.WS_SURVEY_KEY_RANK);
            String motivationGenderListGender = obj.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_GENDER);
            double motivationAgeListProportion = obj.getDouble(QuitSmokeClientConstant.WS_SURVEY_KEY_PROPORTION);
            String motivationAgeListBehaviour = obj.getString(QuitSmokeClientConstant.WS_SURVEY_KEY_BEHAVIOUR);
            String classification = obj.getString(QuitSmokeClientConstant.WS_SURVEY_KEY_CLASSIFICATION);
            result.addMotivationGenderEntity(rank, motivationGenderListGender, motivationAgeListBehaviour, motivationAgeListProportion, classification);
        }

        return result;
    }
}
