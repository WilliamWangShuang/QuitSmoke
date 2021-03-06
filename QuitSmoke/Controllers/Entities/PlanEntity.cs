using System;
using System.Collections;
using Newtonsoft.Json;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class PlanEntity
    {
        public string smoker_name { get; set; }
        public string uid { get; set; }
        public int target_amount { get; set; }
        public int real_amount { get; set; }
        public string plan_create_date { get; set; }
        public string status { get; set; }
        public string encouragement { get; set; }
        public int milestone { get; set; }
        public int successiveDays { get; set; }
        public string reward { get; set; }
    }
}