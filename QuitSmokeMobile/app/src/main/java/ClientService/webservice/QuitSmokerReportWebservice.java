package ClientService.webservice;

import android.util.Log;

import ClientService.Entities.CalculateFrsEntity;
import ClientService.Entities.UpdatePartnerEntity;
import ClientService.Entities.UserInfoEntity;
import ClientService.QuitSmokeClientConstant;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import ClientService.QuitSmokeClientUtils;

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
}
