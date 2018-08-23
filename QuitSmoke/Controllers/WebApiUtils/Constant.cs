namespace QuitSmokeWebAPI.Controllers.WebApiUtils
{
    public class Constant
    {
        public static string FIREBASE_ROOT = "https://quit-smoke-web-api.firebaseio.com/";
        public static string AUTH_ROOT_SIGN_UP = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/signupNewUser?key=";
        public static string AUTH_ROOT_SIGN_IN = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=";
        public static string AUTH_VERIFY_EMAIL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/createAuthUri?key=";
        public static string AUTH_CONTINUE_URI = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/createAuthUri";
        public static string FIREBASE_SUFFIX_JSON = ".json";
        public static string FIREBASE_APP_KEY = "AIzaSyBxuvPNqF9TMk4gea0hfOls1QEQa4hNrlQ";
        public static string FIRBASE_RESPONSE_EMAIL_EXIST = "EMAIL_EXISTS";

        #region <<json keys>>
        public static string JSON_KEY_USER_ID = "user_id";
        public static string JSON_KEY_LICENCE_NO = "licence_no";
        public static string JSON_KEY_LICENCE_TYPE = "licence_type";
        public static string JSON_KEY_EMAIL = "email";
        public static string JSON_KEY_NAME = "name";
        public static string JSON_KEY_PWD = "password";
        public static string JSON_KEY_SMOKER_I = "smoker_indicator";
        public static string JSON_KEY_PARTNER_I = "partner_indicator";
        public static string JSON_KEY_REGISTER_DATE = "register_date";
        public static string JSON_KEY_SUBURB = "suburb";
        public static string JSON_KEY_CITY = "city";
        public static string JSON_KEY_PLAN_ID = "plan_id";
        public static string JSON_KEY_PARTNER_ID = "partner_id";
        public static string JSON_KEY_POINT = "point";
        public static string JSON_KEY_UID = "localId";
        public static string JSON_KEY_CODE = "code";
        public static string JSON_KEY_MESSAGE = "message";
        public static string JSON_KEY_ID_TOKEN = "idToken";
        public static string JSON_KEY_AGE = "age";
        public static string JSON_KEY_REGISTER_STATUS = "registered";

        #endregion

        #region <<json node name>>
        public static string JSON_NODE_NAME_APP_USERS = "app_users";

        #endregion
    }
}