package com.apap.director.im.domain.websocket.service;

import android.util.Log;

import rx.functions.Action1;
import ua.naiksoftware.stomp.client.StompMessage;

public class MessageAction implements Action1<StompMessage> {

    @Override
    public void call(StompMessage stompMessage) {

        //TODO: decode message and add it to database;
        Log.v("HAI/MessageAction", stompMessage.getPayload());

    }
}
