using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using System.Net.Http;
using System.Net.Http.Headers;
using QuitSmokeWebAPI.Controllers.Entity;
using QuitSmokeWebAPI.Controllers.WebApiUtils;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

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
                        //testEntity = JsonConvert.DeserializeObject<MyTestEntity>(json);
                        
                        // JValue jsonVal = JValue.Parse(jsonString) as JValue; 
                        // dynamic rcvData = jsonVal;

                        // testEntity.Name = rcvData.name;
                        // JValue jsonValUnit = JValue.Parse(rcvData.unit) as JValue;
                        // Console.WriteLine("444444444" + jsonValUnit);
                        // foreach (JObject content in jsonValUnit.Children<JObject>())
                        // {
                        //     foreach (JProperty prop in content.Properties())
                        //     {
                        //         testEntity.UnitInfo.Add(prop.Name + prop.Value);
                        //     }
                        // }
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
        public void Post([FromBody] string value)
        {
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
