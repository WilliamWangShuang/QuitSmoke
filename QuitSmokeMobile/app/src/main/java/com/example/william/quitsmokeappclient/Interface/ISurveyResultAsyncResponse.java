package com.example.william.quitsmokeappclient.Interface;

import java.util.List;

import ClientService.Entities.SurveyResultEntity;

public interface ISurveyResultAsyncResponse {
    void processFinish(SurveyResultEntity reponseResult);
}
