using System;
using System.Collections;
using System.Collections.Generic;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class SurveyData 
    {
        public SurveyData()
        {
            MeanEntityList = new List<MeanEntity>();
            ChanceAgeList = new List<ChanceAge>();
            MotivationAgeList = new List<MotivationAge>();
            MotivationGenderList = new List<MotivationGender>();
        }
        public MeanEntity MyMeanGroupEntity { get; set; }
        public List<MeanEntity> MeanEntityList { get; set; }
        public List<ChanceAge> ChanceAgeList { get; set; }
        public List<MotivationAge> MotivationAgeList { get; set; }
        public List<MotivationGender> MotivationGenderList { get; set; }
    }
}