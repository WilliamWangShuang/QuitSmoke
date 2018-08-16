using System;
using System.Collections;
using System.Collections.Generic;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class Unit
    {
        public string FIT5046 { get; set; }
        public string FIT5140 { get; set; }
    }

    public class MyTestEntity
    {
        public string name { get; set; }
        public Unit unit { get; set; }
    }

}