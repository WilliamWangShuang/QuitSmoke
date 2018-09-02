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
        public string plan_id { get; set; }
        public string partner_email { get; set; }
        public int point { get; set; }
        public string uid { get; set; }
        public string age { get; set; }
        public string gender { get; set; }
        public string smoker_node_name { get; set;}
    }
}