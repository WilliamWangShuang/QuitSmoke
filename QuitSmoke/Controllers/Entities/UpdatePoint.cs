using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class UpdatePoint
    {
        public string smokerNodeName { get; set; }
        public bool isReset { get; set; }
    }
}