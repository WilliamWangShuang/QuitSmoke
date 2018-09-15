using System;
using System.Collections;
using System.Collections.Generic;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class NoSmokeEntity
    {
        public List<NoSmokeItem> noSmokeList { get; set; }
        public string type { get; set; }

        public NoSmokeEntity()
        {
            noSmokeList = new List<NoSmokeItem>();
        }

        public class NoSmokeItem
        {
            public string address { get; set; }
            public double latitude { get; set; }
            public double longitude { get; set; }
            public string name { get; set; }
            public string type { get; set; }
        }
    }
}