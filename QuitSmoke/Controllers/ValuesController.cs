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

        // POST api/values
        [HttpPost]
        public string Post([FromBody] AppUser newUser)
        {
            string responseString = null;
            try
            {
                using (var client = new WebClient())
                {
                    // specify encoding 
                    client.Encoding = Encoding.UTF8;
                    // set headers
                    client.Headers.Add("Accept", "application/json");
                    // Serialize our concrete class into a JSON String
                    // var values = new NameValueCollection();
                    // values[Constant.JSON_KEY_LICENCE_NO] = newUser.license_no;
                    // values[Constant.JSON_KEY_LICENCE_TYPE] = newUser.license_type;
                    // values[Constant.JSON_KEY_EMAIL] = newUser.email;
                    // values[Constant.JSON_KEY_NAME] = newUser.name;
                    // values[Constant.JSON_KEY_PWD] = newUser.password;
                    // values[Constant.JSON_KEY_SMOKER_I] = newUser.smoker_indicator;
                    // values[Constant.JSON_KEY_PARTNER_I] = newUser.partner_indicator;
                    // values[Constant.JSON_KEY_REGISTER_DATE] = newUser.register_date;
                    // values[Constant.JSON_KEY_SUBURB] = newUser.suburb;
                    // values[Constant.JSON_KEY_CITY] = newUser.city;
                    // values[Constant.JSON_KEY_PLAN_ID] = newUser.plan_id;
                    // values[Constant.JSON_KEY_PARTNER_ID] = newUser.partner_id;
                    // values[Constant.JSON_KEY_POINT] = newUser.point.ToString();
                    //var values = QuitSmokeUtils.Serialize(newUser);

                    var response = client.UploadString(Constant.FIREBASE_ROOT 
                        + Constant.JSON_NODE_NAME_APP_USERS 
                        + Constant.FIREBASE_SUFFIX_JSON, JsonConvert.SerializeObject(newUser));

                    responseString = JObject.Parse(response).ToString();
                } 
            }
            catch (Exception ex)
            {
                QuitSmokeUtils.WriteErrorStackTrace(ex);
                
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
