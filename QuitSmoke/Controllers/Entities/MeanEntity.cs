using System;
using System.Collections;
using Newtonsoft.Json;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class MeanEntity {
        public int age_end { get; set; }
        public int age_start { get; set; }
        public string gender { get; set; }
        public int mean_consume { get; set; }
    }
}