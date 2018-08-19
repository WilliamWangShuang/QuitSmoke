using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class AppUser
    {
        public string email { get; set; }
        public string password { get; set; }
        public string name { get; set; }
        public string smoker_indicator { get; set; }
        public string partner_indicator { get; set; }
        public string register_date { get; set; }
        public string suburb { get; set; }
        public string city { get; set; }
        public string plan_id { get; set; }
        public string partner_id { get; set; }
        public int point { get; set; }
    }
}