//package com.apap.director.client.data.net.websocket.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import com.apap.director.client.data.net.to.MessageTO;
//import com.apap.director.client.data.util.SimpleBinder;
//import com.apap.director.network.rest.Paths;
//
//import org.java_websocket.WebSocket;
//
//import java.util.Arrays;
//import java.util.HashMap;
//
//import javax.inject.Inject;
//
//import rx.functions.Action1;
//import ua.naiksoftware.stomp.LifecycleEvent;
//import ua.naiksoftware.stomp.Stomp;
//import ua.naiksoftware.stomp.StompHeader;
//import ua.naiksoftware.stomp.client.StompClient;
//import ua.naiksoftware.stomp.client.StompMessage;
//
//public class StompService extends Service {
//
//    private StompClient client;
//    private String cookie;
//    //private Action1<LifecycleEvent> listener;
//
//    @Inject
//    MessageAction messageAction;
//
////    @Inject
////    public StompService(MessageAction messageAction) {
////        this.messageAction = messageAction;
////        listener = new Action1<LifecycleEvent>() {
////            @Override
////            public void call(LifecycleEvent lifecycleEvent) {
////
////                switch (lifecycleEvent.getType()) {
////
////                    case OPENED:
////                        Log.v("HAI/StompService", "Stomp connection opened");
////                        break;
////
////                    case ERROR:
////                        Log.e("HAI/StompService", "Error", lifecycleEvent.getException());
////                        break;
////
////                    case CLOSED:
////                        Log.d("HAI/StompService", "Stomp connection closed");
////                        break;
////
////                }
////            }
////        };
////    }
//
//    public void setUpClient(String cookie){
//        this.cookie = cookie;
//        Log.v("HAI/StompService", "Cookie : "+cookie);
//        HashMap<String, String> connectionHeaders = new HashMap<>();
//        connectionHeaders.put("Cookie", cookie);
//
//        client = Stomp.over(WebSocket.class, "ws://"+ Paths.SERVER_IP+":"+Paths.SERVER_PORT + Paths.WEBSOCKET_ENDPOINT+"/websocket", connectionHeaders);
//
//        Log.v("HAI", "Connecting to websocket with cookie "+ cookie+"...");
//        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
//        client.setUpClient();
//       // client.topic("/user/exchange/amq.direct/messages", Arrays.asList(cookieHeader)).subscribe(messageAction, new ErrorAction());
//       // client.lifecycle().subscribe(listener);
//    }
//
//    public void disconnect(){
//        Log.v("HAI/StompService", "Disconnecting!");
//        client.disconnect();
//    }
//
//    public void sendMessage(String text){
//        Log.v("HAI/StompService", "Sending default message!");
//        Log.v("HAI/StompService", "Cookie " + cookie);
//        client.setUpClient(true);
////        client.disconnect();
////        try {
////            wait(200);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        client.setUpClient();
//        client.send("/app/hello",text);
//    }
//
//    public void sendMessage(String recipientKeyBase64, String text, String from){
////        // TODO: Encode the message and send via stomp client
////        Log.v("HAI/StompService", "Sending msg to " +recipientKeyBase64);
////        Log.v("HAI/StompService", "CLIENT CONNECTED? "+client.isConnected());
////        client.send("/app/hello","Hai<3333333333");
////
////        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
////        StompHeader destinationHeader = new StompHeader("destination", "/app/message/test/"+recipientKeyBase64);
////        StompHeader destinationHeader2 = new StompHeader("destination", "/app/hello");
////        MessageTO frame = new MessageTO(from, text);
////
////        client.send(new StompMessage("SEND", Arrays.asList(destinationHeader, cookieHeader), "hai"));
////        client.send(new StompMessage("SEND", Arrays.asList(destinationHeader2, cookieHeader), "hai"));
//
//    }
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new SimpleBinder(this);
//    }
//}
