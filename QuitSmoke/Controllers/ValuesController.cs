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

        // POST api/Values/Calculatefrs
        [HttpPost("Calculatefrs")]
        public int calculateFRS([FromBody] FRSData frsData)
        {
            int result = 0;
            try
            {
                int age = frsData.age;
                string gender = frsData.gender;
                int total_cholesterol = frsData.total_cholesterol;
                int hdl_cholesterol = frsData.hdl_cholesterol;
                int systolic_blood_pressure = frsData.systolic_blood_pressure;

                // // calculate age
                // switch(age)
                // {
                //     case 20 <= age && age <= 34:
                //         result = -7;
                //         break;
                //     case 

                // }
            } 
            catch (Exception ex)
            {
                QuitSmokeUtils.WriteErrorStackTrace(ex);
            }
            return result;
        }

        // POST api/Values/checkEmail
        [HttpPost]
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
