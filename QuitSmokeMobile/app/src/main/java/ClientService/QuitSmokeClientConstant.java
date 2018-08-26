package ClientService;

public class QuitSmokeClientConstant {
    // web service URL
    public static String WEB_SERVER_BASE_URI = "https://quit-smoke-web-api.appspot.com/";
    public static String LOGIN_URI_WS = "api/Values/login";
    public static String REGISTER_WS = "api/Values";
    public static String UPDATE_PARTNER_WS = "api/Values/updatePartner";
    public static String CALCULATE_FRS = "api/Values/calculatefrs";
    public static String CHECK_USER_EXIST_WS = "/checkEmail";

    public static String WS_KEY_EXCEPTION = "Exception";
    public static String MSG_401 = "401 Unauthorized. Please check your identity";
    public static String MSG_404 = "404 Web resource not found";
    public static String MSG_500 = "500 Internal error when request web resource";
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String SUCCESS_MSG = "Success";
    public static String EMAIL_EXIST = "EMAIL_EXISTS";

    // app user json key
    public static String WS_JSON_USER_KEY_CITY = "city";
    public static String WS_JSON_USER_KEY_EMAIL = "email";
    public static String WS_JSON_USER_KEY_NAME = "name";
    public static String WS_JSON_USER_KEY_PARTNER_ID = "partner_id";
    public static String WS_JSON_USER_KEY_PARTNER_I = "partner_indicator";
    public static String WS_JSON_USER_KEY_SMOKER_I = "smoker_indicator";
    public static String WS_JSON_USER_PASSWORD = "password";
    public static String WS_JSON_USER_KEY_PLAN_ID = "plan_id";
    public static String WS_JSON_USER_KEY_POINT = "point";
    public static String WS_JSON_USER_KEY_REGISTER_DT = "register_date";
    public static String WS_JSON_USER_KEY_SUBURB = "suburb";
    public static String WS_JSON_USER_KEY_UID = "uid";
    public static String WS_JSON_USER_KEY_AGE = "age";
    public static String WS_JSON_USER_KEY_GENDER = "gender";

    // update user json key
    public static String WS_JSON_UPDATE_PARTNER_KEY_SMOKER_NODE_NAME = "smokerNodeName";
    public static String WS_JSON_UPDATE_PARTNER_KEY_SMOKER_PARTNER_EMAIL = "partnerEmail";

    // calculate Framingham Risk Score json key
    public static String WS_CALCULATE_FRS_KEY_CHOL = "total_cholesterol";
    public static String WS_CALCULATE_FRS_KEY_HDL = "hdl_cholesterol";
    public static String WS_CALCULATE_FRS_KEY_SBP = "systolic_blood_pressure";
    public static String WS_CALCULATE_FRS_KEY_TREATED_I = "isTreated";
}
