using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class ChanceAge 
    {
        public string behaviour { get; set; }
        public int age_end { get; set; }
        public int age_start { get; set; }
        public double population_proportion { get; set; }
    }
}