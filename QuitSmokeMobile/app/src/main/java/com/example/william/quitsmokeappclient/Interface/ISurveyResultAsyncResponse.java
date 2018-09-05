package com.example.william.quitsmokeappclient.Interface;

import java.util.List;
import clientservice.entities.SurveyResultEntity;

public interface ISurveyResultAsyncResponse {
    void processFinish(SurveyResultEntity reponseResult);
}
