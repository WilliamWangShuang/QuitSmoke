package com.quitsmoke.william.quitsmokeappclient.Interface;

import java.util.ArrayList;

import clientservice.entities.PlanEntity;

public interface IGetPendingPlanResultAsyncResponse {
    void processFinish(ArrayList<PlanEntity> reponseResult);
}
