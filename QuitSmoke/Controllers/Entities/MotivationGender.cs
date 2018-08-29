using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class MotivationGender 
    {
        public int rank { get; set; }
        public string gender { get; set; }
        public string behaviour { get; set; }
        public double population_proportion { get; set; }
        public string variable_classification_to_age { get; set; }
    }
}