package com.quitsmoke.william.quitsmokeappclient.Interface;

import java.util.List;

import clientservice.entities.NoSmokePlace;

public interface ILoadNoSmokePlaceAsyncResponse {
    void processFinish(List<NoSmokePlace> noSmokePlaces);
}
