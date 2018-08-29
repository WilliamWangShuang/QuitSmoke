using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using System.Net.Http;
using System.Net.Http.Headers;
using QuitSmokeWebAPI.Controllers.Entity;
using QuitSmokeWebAPI.Controllers.WebApiUtils;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System.Threading.Tasks;
using System.Collections.Specialized;
using System.Text;
using System.Net;

namespace QuitSmokeWebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ValuesController : ControllerBase
    {
        // GET api/values
        [HttpGet]
        public ActionResult<IEnumerable<string>> Get()
        {
            return new string[] { "This is Restful deploy test." };
        }

        // GET api/values/5
        [HttpGet("{id}")]
        public ActionResult<MyTestEntity> Get(int id)
        {
            // declare a http client to call RESTful from web api exposed by other providers
            using(var client = new HttpClient())
            {
                MyTestEntity testEntity = new MyTestEntity();
                try 
                {
                    //testEntity.UnitInfo = new System.Collections.ArrayList();
                    client.BaseAddress = new Uri(Constant.FIREBASE_ROOT);

                    // add an Accept header for JSON format
                    client.DefaultRequestHeaders.Accept.Add(
                        new MediaTypeWithQualityHeaderValue("application/json"));
                    

                    // retrieve data response
                    HttpResponseMessage response = client.GetAsync(Constant.FIREBASE_ROOT + Constant.FIREBASE_SUFFIX_JSON).Result;
                    if (response.IsSuccessStatusCode)
                    {
                        // parse the response body
                        testEntity = response.Content.ReadAsAsync<MyTestEntity>().Result;
                    } else {
                        testEntity.name = "Unknown";
                        testEntity.unit.FIT5046 = "No enrolled unit for FIT5046";
                        testEntity.unit.FIT5140 = "No enrolled unit for FIT5140";
                    }
                } 
                catch (Exception ex)
                {
                    throw ex;
                }
                return testEntity;
            }
        }

        // POST api/values/login
        [HttpPost("login")]
        public ActionResult<AppUser> Login([FromBody] AppUser user)
        {
            AppUser result = null;
            try
            {
                JObject responseAuth = null;
                // get uid by email and password
                using(var authClient = new WebClient())
                {
                    // specify encoding 
                    authClient.Encoding = Encoding.UTF8;
                    // set headers
                    authClient.Headers.Add("Accept", "application/json");
                    var response = authClient.UploadString(Constant.AUTH_ROOT_SIGN_IN
                        + Constant.FIREBASE_APP_KEY, JsonConvert.SerializeObject(new AuthUser() { email = user.email, password = user.password, returnSecureToken = true }));

                    responseAuth = JObject.Parse(response);
                }
                
                // get user info if uid exists
                if (responseAuth.GetValue(Constant.JSON_KEY_UID) != null)
                {
                    // get user id
                    string uid = responseAuth.GetValue(Constant.JSON_KEY_UID).Value<string>();
                    // get user info by uid
                    using(var client = new HttpClient())
                    {
                        //testEntity.UnitInfo = new System.Collections.ArrayList();
                        client.BaseAddress = new Uri(Constant.FIREBASE_ROOT);

                        // add an Accept header for JSON format
                        client.DefaultRequestHeaders.Accept.Add(
                            new MediaTypeWithQualityHeaderValue("application/json"));
                        

                        // retrieve data response
                        HttpResponseMessage response = client.GetAsync(Constant.FIREBASE_ROOT 
                            + Constant.JSON_NODE_NAME_APP_USERS 
                            + Constant.FIREBASE_SUFFIX_JSON
                            + string.Format("?orderBy=\"uid\"&equalTo={0}&print=pretty", "\"" + uid + "\"")).Result;
                        if (response.IsSuccessStatusCode)
                        {
                            // parse the response body
                            JObject firebaseQueryResult = response.Content.ReadAsAsync<JObject>().Result;
                            UserInfo userInfo = firebaseQueryResult.First.First.ToObject<UserInfo>();
                            // parse AppUser object
                            result = new AppUser();
                            result.uid = uid;
                            result.name = userInfo.name;
                            result.partner_id = userInfo.partner_id;
                            result.partner_indicator = userInfo.partner_indicator;
                            result.point = userInfo.point;
                            result.register_date = userInfo.register_date;
                            result.smoker_indicator = userInfo.smoker_indicator;
                            result.suburb = userInfo.suburb;
                            result.city = userInfo.city;
                            result.gender = userInfo.gender;
                            result.age = userInfo.age.ToString();
                        } 
                    }
                }
            }
            catch (Exception ex)
            {
                result = null;
                QuitSmokeUtils.WriteErrorStackTrace(ex);
            }
            return result;
        }

        // POST api/Values/calculatefrs
        [HttpPost("calculatefrs")]
        public int calculateFRS([FromBody] FRSData frsData)
        {
            int result = 0;
            try
            {
                // calculate age
                result = calculateFRSResult(frsData);
            } 
            catch (Exception ex)
            {
                QuitSmokeUtils.WriteErrorStackTrace(ex);
            }
            return result;
        }

        private int calculateFRSResult(FRSData frsData) 
        {
            int result = 0;

            int age = frsData.age;
            string gender = frsData.gender;
            int total_cholesterol = frsData.total_cholesterol;
            int hdl_cholesterol = frsData.hdl_cholesterol;
            int systolic_blood_pressure = frsData.systolic_blood_pressure;
            bool isTreated = frsData.isTreated;
            bool isMale = Constant.GENDER_M.Equals(gender, StringComparison.InvariantCultureIgnoreCase);

            // check age point
            #region <<age>>
            if (20 <= age && age <= 34)
                result = result + (isMale ? -9 : -7);
            else if(35 <= age && age <= 39)
                result = result + (isMale ? -4 : -3);
            else if(40 <= age && age <= 44)
                result = result + (isMale ? 0 : 0);
            else if(45 <= age && age <= 49)
                result = result + (isMale ? 3 : 3);
            else if(50 <= age && age <= 54)
                result = result + (isMale ? 6 : 6);
            else if(55 <= age && age <= 59)
                result = result + (isMale ? 8 : 8);
            else if(60 <= age && age <= 64)
                result = result + (isMale ? 10 : 10);
            else if(65 <= age && age <= 69)
                result = result + (isMale ? 11 : 12);
            else if(70 <= age && age <= 74)
                result = result + (isMale ? 12 : 14);
            else if(75 <= age && age <= 79)
                result = result + (isMale ? 13 : 16);
            #endregion
            #region <<Total cholesterol, mg/dL>>
            if(20 <= age && age <= 29)
            {
                if(total_cholesterol < 160)
                {
                    result = result + (isMale ? 0 : 0);
                }
                else if(160 <= total_cholesterol && total_cholesterol <= 199)
                {
                    result = result + (isMale ? 4 : 4);
                }
                else if(200 <= total_cholesterol && total_cholesterol <= 239)
                {
                    result = result + (isMale ? 7 : 8);
                }
                else if(240 <= total_cholesterol && total_cholesterol <= 279)
                {
                    result = result + (isMale ? 9 : 11);
                }
                else if(total_cholesterol >= 280)
                {
                    result = result + (isMale ? 11 : 13);
                }
            }
            else if(40 <= age && age <= 49)
            {
                if(total_cholesterol < 160)
                {
                    result = result + (isMale ? 0 : 0);
                }
                else if(160 <= total_cholesterol && total_cholesterol <= 199)
                {
                    result = result + (isMale ? 3 : 3);
                }
                else if(200 <= total_cholesterol && total_cholesterol <= 239)
                {
                    result = result + (isMale ? 5 : 6);
                }
                else if(240 <= total_cholesterol && total_cholesterol <= 279)
                {
                    result = result + (isMale ? 6 : 8);
                }
                else if(total_cholesterol >= 280)
                {
                    result = result + (isMale ? 8 : 10);
                }
            }
            else if(50 <= age && age <= 59)
            {
                if(total_cholesterol < 160)
                {
                    result = result + (isMale ? 0 : 0);
                }
                else if(160 <= total_cholesterol && total_cholesterol <= 199)
                {
                    result = result + (isMale ? 2 : 2);
                }
                else if(200 <= total_cholesterol && total_cholesterol <= 239)
                {
                    result = result + (isMale ? 3 : 4);
                }
                else if(240 <= total_cholesterol && total_cholesterol <= 279)
                {
                    result = result + (isMale ? 4 : 5);
                }
                else if(total_cholesterol >= 280)
                {
                    result = result + (isMale ? 5 : 7);
                }
            }
            else if(60 <= age && age <= 69)
            {
                if(total_cholesterol < 160)
                {
                    result = result + (isMale ? 0 : 0);
                }
                else if(160 <= total_cholesterol && total_cholesterol <= 199)
                {
                    result = result + (isMale ? 1 : 1);
                }
                else if(200 <= total_cholesterol && total_cholesterol <= 239)
                {
                    result = result + (isMale ? 1 : 2);
                }
                else if(240 <= total_cholesterol && total_cholesterol <= 279)
                {
                    result = result + (isMale ? 2 : 3);
                }
                else if(total_cholesterol >= 280)
                {
                    result = result + (isMale ? 3 : 4);
                }
            }
            else if(70 <= age && age <= 79)
            {
                if(total_cholesterol < 160)
                {
                    result = result + (isMale ? 0 : 0);
                }
                else if(160 <= total_cholesterol && total_cholesterol <= 199)
                {
                    result = result + (isMale ? 0 : 1);
                }
                else if(200 <= total_cholesterol && total_cholesterol <= 239)
                {
                    result = result + (isMale ? 0 : 1);
                }
                else if(240 <= total_cholesterol && total_cholesterol <= 279)
                {
                    result = result + (isMale ? 1 : 2);
                }
                else if(total_cholesterol >= 280)
                {
                    result = result + (isMale ? 1 : 2);
                }
            }
            #endregion
            #region <<If cigarette smoker>>
            if(20 <= age && age <= 39)
                result = result + (isMale ? 8 : 9);
            else if(40 <= age && age <= 49)
                result = result + (isMale ? 5 : 7);
            else if(50 <= age && age <= 59)
                result = result + (isMale ? 3 : 4);
            else if(60 <= age && age <= 69)
                result = result + (isMale ? 1 : 2);
            else if(70 <= age && age <= 79)
                result = result + (isMale ? 1 : 1);
            #endregion
            #region <<HDL cholesterol, mg/dL>>
            if(hdl_cholesterol >= 60)
                result = result + (isMale ? -1 : -1);
            else if(50 <= hdl_cholesterol && hdl_cholesterol <= 59)
                result = result + (isMale ? 0 : 0);
            else if(40 <= hdl_cholesterol && hdl_cholesterol <= 49)
                result = result + (isMale ? 1 : 1);
            else if(hdl_cholesterol < 40)
                result = result + (isMale ? 2 : 2);
            #endregion
            #region <<Systolic blood pressure, mm Hg>>
            if(isTreated) 
            {
                if(systolic_blood_pressure < 120)
                    result = result + (isMale ? 0 : 0);
                else if(120 <= systolic_blood_pressure && systolic_blood_pressure <= 129)
                {
                    result = result + (isMale ? 1 : 3);
                }
                else if(130 <= systolic_blood_pressure && systolic_blood_pressure <= 139)
                {
                    result = result + (isMale ? 2 : 4);
                }
                else if(140 <= systolic_blood_pressure && systolic_blood_pressure <= 159)
                {
                    result = result + (isMale ? 2 : 5);
                }
                else if(160 <= systolic_blood_pressure)
                {
                    result = result + (isMale ? 3 : 6);
                }
            }
            else
            {
                if(systolic_blood_pressure < 120)
                    result = result + (isMale ? 0 : 0);
                else if(120 <= systolic_blood_pressure && systolic_blood_pressure <= 129)
                {
                    result = result + (isMale ? 0 : 1);
                }
                else if(130 <= systolic_blood_pressure && systolic_blood_pressure <= 139)
                {
                    result = result + (isMale ? 1 : 2);
                }
                else if(140 <= systolic_blood_pressure && systolic_blood_pressure <= 159)
                {
                    result = result + (isMale ? 1 : 3);
                }
                else if(160 <= systolic_blood_pressure)
                {
                    result = result + (isMale ? 2 : 4);
                }
            }
            #endregion
            
            return result;
        }

        [HttpPost("GetSurveyData")]
        public ActionResult<SurveyData> getSurveyData([FromBody] SurveyRequest surveyReq)
        {
            SurveyData surveyData = new SurveyData();
            try
            {
                // get value from request parameters
                string gender = surveyReq.Gender;
                int age = Int32.Parse(surveyReq.Age);
                int smokeNo = Int32.Parse(surveyReq.smokeNo);

                // do mean logic
                using(var client = new HttpClient())
                {
                    client.BaseAddress = new Uri(Constant.FIREBASE_ROOT);

                    // add an Accept header for JSON format
                    client.DefaultRequestHeaders.Accept.Add(
                        new MediaTypeWithQualityHeaderValue("application/json"));
                    
                    // get age range number
                    int endAge = 0;
                    int startAge = getAgeRange(Int32.Parse(surveyReq.Age), out endAge);
                    HttpResponseMessage response = client.GetAsync(Constant.FIREBASE_ROOT + Constant.FIREBASE_SUFFIX_JSON).Result;
 
                    if (response.IsSuccessStatusCode)
                    {
                        // get the response json object (top-level)
                        JObject topJObject = response.Content.ReadAsAsync<JObject>().Result;;
                        #region <<mean logic>> 
                        // get mean json object
                        JObject meanJson = topJObject.GetValue(Constant.JSON_NODE_NAME_MEAN_AGE_GENDER).ToObject<JObject>();
                        List<MeanEntity> meanList = new List<MeanEntity>();
                        foreach (JToken token in meanJson.Children())
                        {
                            MeanEntity entity = token.First.ToObject<MeanEntity>();
                            meanList.Add(entity);
                        }
                        surveyData.MeanEntityList = meanList.FindAll(m => surveyReq.Gender.Equals(m.gender, StringComparison.InvariantCultureIgnoreCase));
                        surveyData.MyMeanGroupEntity = surveyData.MeanEntityList.Find(
                            m => m.age_start == startAge && m.age_end == endAge && m.gender.Equals(surveyReq.Gender));
                        #endregion
                        #region <<quit chance logic>>
                        JObject chanceAgeJson = topJObject.GetValue(Constant.JSON_NODE_CHANCE_AGE).ToObject<JObject>();
                        List<ChanceAge> chanceAgeList = new List<ChanceAge>();
                        foreach (JToken token in chanceAgeJson.Children())
                        {
                            ChanceAge entity = token.First.ToObject<ChanceAge>();
                            chanceAgeList.Add(entity);
                        }
                        surveyData.ChanceAgeList = chanceAgeList.FindAll(
                            ca => Constant.FAIL_REDUCE_AMOUNT.Equals(ca.behaviour, StringComparison.InvariantCultureIgnoreCase)
                                    || Constant.FAIL_COLD_TURKEY.Equals(ca.behaviour, StringComparison.InvariantCultureIgnoreCase)
                                    || Constant.SUCC_COLD_TURKEY.Equals(ca.behaviour, StringComparison.InvariantCultureIgnoreCase)
                                    || Constant.SUCC_REDUCE_AMOUNT.Equals(ca.behaviour, StringComparison.InvariantCultureIgnoreCase));
                        #endregion
                        #region <<motivation age logic>>
                        JObject motivationAgeJson = topJObject.GetValue(Constant.JSON_NODE_MOTIVATION_AGE).ToObject<JObject>();
                        List<MotivationAge> motivationAgeList = new List<MotivationAge>();
                        foreach (JToken token in motivationAgeJson.Children())
                        {
                            MotivationAge entity = token.First.ToObject<MotivationAge>();
                            motivationAgeList.Add(entity);
                        }
                        surveyData.MotivationAgeList = motivationAgeList.FindAll(ma => ma.age_start == startAge && ma.age_end == endAge);
                        #endregion
                        #region <<motivation gender logic>>
                        JObject motivationGenderJson = topJObject.GetValue(Constant.JSON_NODE_MOTIVATION_GENDER).ToObject<JObject>();
                        List<MotivationGender> motivationGenderList = new List<MotivationGender>();
                        foreach (JToken token in motivationGenderJson.Children())
                        {
                            MotivationGender entity = token.First.ToObject<MotivationGender>();
                            motivationGenderList.Add(entity);
                        }
                        surveyData.MotivationGenderList = motivationGenderList.FindAll(mg => mg.gender.Equals(surveyReq.Gender, StringComparison.InvariantCultureIgnoreCase));
                        #endregion
                    } else {
                        throw new Exception(response.RequestMessage + "\n" + response.Content.ToString());
                    }
                    
                }                
            } 
            catch (Exception ex)
            {
                QuitSmokeUtils.WriteErrorStackTrace(ex);
                surveyData = null;
            }

            return surveyData;
        }

        // POST api/Values/checkEmail
        [HttpPost("checkEmail")]
        public bool CheckEmail([FromBody] String email) 
        {
            bool result = false;    
            String response = null;
            JObject responseAuth = null;
            CheckEmailEntity checkEmailEntity = new CheckEmailEntity();
            try 
            {
                using (var authClient = new WebClient())
                {
                    // specify encoding 
                    authClient.Encoding = Encoding.UTF8;
                    // set headers
                    authClient.Headers.Add("Accept", "application/json");
                    checkEmailEntity.identifier = email;
                    checkEmailEntity.continueUri = Constant.AUTH_CONTINUE_URI;
                    response = authClient.UploadString(Constant.AUTH_VERIFY_EMAIL
                        + Constant.FIREBASE_APP_KEY, JsonConvert.SerializeObject(checkEmailEntity));

                    responseAuth = JObject.Parse(response);
                    // get registered status
                    result = responseAuth.GetValue(Constant.JSON_KEY_REGISTER_STATUS).Value<bool>();
                }
            }
            catch (Exception ex)
            {
                QuitSmokeUtils.WriteErrorStackTrace(ex);
            }
            return result;
        }

        [HttpPost("updatePartner")]
        public bool updatePartner([FromBody] UpdatePartner updatePartner)
        {
            bool result = false;
            string nodeName = updatePartner.smokerNodeName;

            try
            {
                // if node name is not empty, start update
                if (!string.IsNullOrEmpty(nodeName))
                {
                    // construct patch uri
                    string uri = Constant.FIREBASE_ROOT 
                        + Constant.JSON_NODE_NAME_APP_USERS + "/"
                        + nodeName + "/"
                        + Constant.FIREBASE_SUFFIX_JSON;
                    
                    using (var client = new HttpClient())
                    {
                        var method = new HttpMethod("PATCH");
                        // make patch request content
                        string json = "{\"" + Constant.JSON_KEY_PARTNER_EMAIL + "\":\"" + updatePartner.partnerEmail +"\"}";
                        HttpContent httpContent = new StringContent(json, Encoding.UTF8, "application/json");
                        var request = new HttpRequestMessage(method, uri)
                        {
                            Content = httpContent
                        };

                        HttpResponseMessage response = new HttpResponseMessage();
                        response = client.SendAsync(request).Result;
                        result = response.IsSuccessStatusCode;
                    }
                    
                }
                
            } catch (Exception ex) {
                QuitSmokeUtils.WriteErrorStackTrace(ex);
                result = false;
            }

            return result;
        }

        // POST api/values
        [HttpPost]
        public string Post([FromBody] AppUser newUser)
        {
            string responseString = null;
            JObject responseAuth = null;
            string response = null;
            try
            {
                using (var authClient = new WebClient())
                {
                    // specify encoding 
                    authClient.Encoding = Encoding.UTF8;
                    // set headers
                    authClient.Headers.Add("Accept", "application/json");
                    response = authClient.UploadString(Constant.AUTH_ROOT_SIGN_UP
                        + Constant.FIREBASE_APP_KEY, JsonConvert.SerializeObject(new AuthUser() { email = newUser.email, password = newUser.password, returnSecureToken = true }));

                    responseAuth = JObject.Parse(response);
                }
                // save user profile after success register this new user auth account in firebase(db)
                responseString = saveNewUserProfile(newUser, responseAuth);
            }
            catch (WebException ex)
            {
                QuitSmokeUtils.WriteErrorStackTrace(ex);
                if (ex.Message.Contains("400")) 
                {
                    responseString = Constant.FIRBASE_RESPONSE_EMAIL_EXIST;
                }
            }
            catch (Exception ex)
            {
                QuitSmokeUtils.WriteErrorStackTrace(ex);
            }
            return responseString;
        }

        // save new user info in db, e.g.city, suburb, registration date, etc.
        private static string saveNewUserProfile(AppUser newUser, JObject responseAuth)
        {
            string responseString;
            // save new user info in firebase
            using (var client = new WebClient())
            {
                // get uid after sign up this new user in firebase
                string uid = responseAuth.GetValue(Constant.JSON_KEY_UID).Value<string>();
                // construct user info object association with the uid
                UserInfo userInfo = new UserInfo();
                userInfo.city = newUser.city;
                userInfo.name = newUser.name;
                userInfo.partner_id = newUser.partner_id;
                userInfo.partner_indicator = newUser.partner_indicator;
                userInfo.point = newUser.point;
                userInfo.register_date = newUser.register_date;
                userInfo.smoker_indicator = newUser.smoker_indicator;
                userInfo.suburb = newUser.suburb;
                userInfo.uid = uid;
                userInfo.age = Int32.Parse(newUser.age);
                userInfo.gender = newUser.gender;

                // specify encoding 
                client.Encoding = Encoding.UTF8;
                // set headers
                client.Headers.Add("Accept", "application/json");
                var response = client.UploadString(Constant.FIREBASE_ROOT
                    + Constant.JSON_NODE_NAME_APP_USERS
                    + Constant.FIREBASE_SUFFIX_JSON, JsonConvert.SerializeObject(userInfo));

                responseString = JObject.Parse(response).ToString();
            }

            return responseString;
        }

        private int getAgeRange(int age, out int endAge)
        {
            int result = 0;
            endAge = 0;
            if (age >= 18 && age <= 24)
            {
                result = 18;
                endAge = 24;
            }
            else if (age >= 25 && age <= 29)
            {
                result = 25;
                endAge = 29;
            }
            else if (age >= 30 && age <= 39)
            {
                result = 30;
                endAge = 39;
            }
            else if (age >= 40 && age <= 49)
            {
                result = 40;
                endAge = 49;
            }
            else if (age >= 50 && age <= 59)
            {
                result = 50;
                endAge = 59;
            }
            else if (age >= 60 && age <= 69)
            {
                result = 60;
                endAge = 69;
            }
            else if (age >= 70)
            {
                result = 70;
                endAge = 200;
            }

            return result;
        }


        // PUT api/values/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/values/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
