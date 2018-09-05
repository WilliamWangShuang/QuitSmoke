package clientservice;

public class QuitSmokeClientConstant {
    // web service URL
    public static String WEB_SERVER_BASE_URI = "https://quit-smoke-web-api.appspot.com/";
    public static String LOGIN_URI_WS = "api/Values/login";
    public static String REGISTER_WS = "api/Values";
    public static String GET_SERVEY_DATA ="api/Values/GetSurveyData";
    public static String UPDATE_PARTNER_WS = "api/Values/updatePartner";
    public static String CALCULATE_FRS = "api/Values/calculatefrs";
    public static String CHECK_USER_EXIST_WS = "/checkEmail";
    public static String CREATE_PLAN_WS = "api/Values/createPlan";
    public static String CHECK_PARTNER = "api/Values/checkPartner";

    // general constants
    public static String WS_KEY_EXCEPTION = "Exception";
    public static String MSG_401 = "401 Unauthorized. Please check your identity";
    public static String MSG_404 = "404 Web resource not found";
    public static String MSG_500 = "500 Internal error when request web resource";
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String SUCCESS_MSG = "Success";
    public static String EMAIL_EXIST = "EMAIL_EXISTS";
    public static String STATUS_OPEN = "open";
    public static String STATUS_PENDING = "pending";
    public static String STATUS_APPROVE = "approve";
    public static String STATUS_CLOSE = "close";

    // app user json key
    public static String WS_JSON_USER_KEY_EMAIL = "email";
    public static String WS_JSON_USER_KEY_NAME = "name";
    public static String WS_JSON_USER_KEY_PARTNER_NAME = "partner_emal";
    public static String WS_JSON_USER_KEY_PARTNER_I = "partner_indicator";
    public static String WS_JSON_USER_KEY_SMOKER_I = "smoker_indicator";
    public static String WS_JSON_USER_PASSWORD = "password";
    public static String WS_JSON_USER_KEY_POINT = "point";
    public static String WS_JSON_USER_KEY_REGISTER_DT = "register_date";
    public static String WS_JSON_USER_KEY_UID = "uid";
    public static String WS_JSON_USER_KEY_AGE = "age";
    public static String WS_JSON_USER_KEY_GENDER = "gender";
    public static String WS_JSON_USER_KEY_SMOKER_NODE_NAME = "smoker_node_name";

    // update user json key
    public static String WS_JSON_UPDATE_PARTNER_KEY_SMOKER_NODE_NAME = "smokerNodeName";
    public static String WS_JSON_UPDATE_PARTNER_KEY_SMOKER_PARTNER_EMAIL = "partnerEmail";

    // calculate Framingham Risk Score json key
    public static String WS_CALCULATE_FRS_KEY_CHOL = "total_cholesterol";
    public static String WS_CALCULATE_FRS_KEY_HDL = "hdl_cholesterol";
    public static String WS_CALCULATE_FRS_KEY_SBP = "systolic_blood_pressure";
    public static String WS_CALCULATE_FRS_KEY_TREATED_I = "isTreated";

    // survey json key
    public static String WS_SURVEY_KEY_SMOKE_NO = "smokeNo";
    public static String WS_SURVEY_KEY_MY_MEAN_GROUP = "myMeanGroupEntity";
    public static String WS_SURVEY_KEY_AGE_END = "age_end";
    public static String WS_SURVEY_KEY_AGE_START = "age_start";
    public static String WS_SURVEY_KEY_MEAN_CONSUME = "mean_consume";
    public static String WS_SURVEY_KEY_MEAN_LIST = "meanEntityList";
    public static String WS_SURVEY_KEY_CHANCE = "chanceAgeList";
    public static String WS_SURVEY_KEY_BEHAVIOUR = "behaviour";
    public static String WS_SURVEY_KEY_PROPORTION = "population_proportion";
    public static String WS_SURVEY_KEY_MOTIVATION_AGE = "motivationAgeList";
    public static String WS_SURVEY_KEY_RANK = "rank";
    public static String WS_SURVEY_KEY_MOTIVATION_GENDER = "motivationGenderList";
    public static String WS_SURVEY_KEY_CLASSIFICATION = "variable_classification_to_age";

    // interactive part json key
    public static String WS_INTERACT_PLAN_TARGET_AMOUNT = "target_amount";
    public static String WS_INTERACT_PLAN_CREATE_DT = "plan_create_date";
    public static String WS_INTERACT_PLAN_STATUS = "status";
    public static String WS_INTERACT_PLAN_REAL_AMOUNT = "real_amount";
}
