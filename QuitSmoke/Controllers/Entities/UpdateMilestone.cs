using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class UpdateMilestone
    {
        public string uid { get; set; }
        public int targetAmount { get; set; }
        public string reward { get; set; }
    }
}