namespace QuitSmokeWebAPI.Controllers.WebApiUtils
{
    public class Constant
    {
        public static string FIREBASE_ROOT = "https://quit-smoke-web-api.firebaseio.com/";
        public static string FIREBASE_SUFFIX_JSON = ".json";

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

        #endregion

        #region <<json node name>>
        public static string JSON_NODE_NAME_APP_USERS = "app_users";

        #endregion
    }
}