package com.apap.director.client.data.net.service;

import com.apap.director.client.data.net.rest.Paths;

import org.java_websocket.WebSocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by Alicja Michniewicz on 02/07/2017.
 */

public class WebSocketService {

    private static final String TAG = WebSocketService.class.getSimpleName();
    private static final String WEBSOCKET_ADRESS = "ws://" + Paths.SERVER_IP + ":" + Paths.SERVER_PORT + Paths.WEBSOCKET_ENDPOINT + "/websocket";
    private static final String INBOX_PATH = "/user/exchange/amq.direct/messages";
    private static final String MESSAGE_ENDPOINT_PATH = "/app/message/test";
    private static final String COOKIE_KEY = "Cookie";

    private StompClient client;

    public void createClient(String cookie) {
        if(client != null) return;
        client = Stomp.over(WebSocket.class, WEBSOCKET_ADRESS, prepareConnectionHeaders(cookie));
    }

    public void destroyClient() {
        if(client != null) {
            client.disconnect();
            client = null;
        }
    }

    public void connect() {
        client.connect();
    }

    public void disconnect() {
        client.disconnect();
    }

    //TODO leak - wait for RxJava2 comaptibility or figure out sth else
    public Observable<StompMessage> getMessages(String cookie) {
        PublishSubject<StompMessage> eventBus = PublishSubject.create();

        return  eventBus.doOnSubscribe(disposable -> client.topic(INBOX_PATH, prepareStompHeaders(cookie))
                .subscribe(eventBus::onNext));
    }

    //TODO leak - wait for RxJava2 comaptibility or figure out sth else
    public Observable<Void> sendMessage(String message, String recipentId) {
        PublishSubject<Void> eventBus = PublishSubject.create();

        return eventBus.doOnSubscribe(disposable -> client.send(String.format("%s/%s", MESSAGE_ENDPOINT_PATH, recipentId), message)
                .subscribe(eventBus::onNext));
    }

    private HashMap<String, String> prepareConnectionHeaders(String cookie) {
        HashMap<String, String> connectionHeaders = new HashMap<>();
        connectionHeaders.put(COOKIE_KEY, cookie);
        return connectionHeaders;
    }

    private List<StompHeader> prepareStompHeaders(String cookie) {
        return Collections.singletonList(new StompHeader(COOKIE_KEY, cookie));
    }

}
