using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class UserInfo
    {
        public string uid { get; set; }
        public string name { get; set; }
        public string smoker_indicator { get; set; }
        public string partner_indicator { get; set; }
        public string partner_email { get; set; }
        public string register_date { get; set; }
        public int point { get; set; }
        public int age { get; set; }
        public string gender { get; set; }
    }
}