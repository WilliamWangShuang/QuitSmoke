package com.example.william.quitsmokeappclient.Interface;

import clientservice.entities.SurveyResultEntity;

public interface ISurveyResultAsyncResponse {
    void processFinish(SurveyResultEntity reponseResult);
}
