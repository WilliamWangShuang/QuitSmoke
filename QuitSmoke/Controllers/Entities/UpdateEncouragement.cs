using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class UpdateEncouragement
    {
        public string smokerUID { get; set; }
        public string createDT { get; set; }
        public string encourage { get; set; }
    }
}