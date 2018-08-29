using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class MotivationAge 
    {
        public int age_start { get; set; }
        public int age_end { get; set; }
        public string behaviour { get; set; }
        public double population_proportion { get; set; }
    }
}