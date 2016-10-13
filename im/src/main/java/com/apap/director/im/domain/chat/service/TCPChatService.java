package com.apap.director.im.domain.chat.service;

import android.os.AsyncTask;
import android.util.Log;

import com.apap.director.im.domain.chat.event.ChatEventListener;
import com.apap.director.im.domain.message.event.MessageEventListener;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

import javax.inject.Inject;

public class TCPChatService extends AbstractChatService {

    public TCPChatService() {
    }

    @Inject
    public MessageEventListener testlistener;
    //TODO: Move async tasks to other classes
    //TODO: Clean the code

    @Override
    public void login(final String username, final String password){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                Log.v("HAI/Async", "Login :)");
                try {
                    connection.login(username, password);
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.v("HAI/Async", "Authenticated: "+String.valueOf(connection.isAuthenticated()));


                return null;
            }

        }.execute();

    }

    @Override
    public void connect(final String host, final int port){

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {


                if(testlistener == null){
                    Log.v("HAI/TCPChatService", "NULL");
                }
                else{
                    Log.v("HAI/TCPChatService", "NOT NULL");
                }

                XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
                connection = new XMPPTCPConnection(builder.setConnectTimeout(10000).setHost(host).setServiceName("ala-pc").setPort(port).setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled).build());

                try {
                    connection.setPacketReplyTimeout(10000);
                    connection.connect();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }

                Log.v("HAI/Async", "HAI :)");
                chatManager = ChatManager.getInstanceFor(connection);
                chatManager.addChatListener(new ChatEventListener());
                Log.v("HAI/Async", String.valueOf(connection.isConnected()));
                return null;
            }
        }.execute();

    }

    @Override
    public void sendMessage(final String to, final String body) {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                Log.v("HAI/Async", "Message :)");
                try {
                    org.jivesoftware.smack.chat.Chat chat = chatManager.createChat(to);
                    Log.v("HAI/AbstractChatService", "Sending message to "+to);
                    chat.sendMessage("STRING MSG");
                    Message message = new Message();
                    message.setBody(body);
                    message.setType(Message.Type.chat);
                    chat.sendMessage(message);
                } catch (SmackException.NotConnectedException e1) {
                    e1.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                }

                Log.v("HAI/Async", "Authenticated: "+String.valueOf(connection.isAuthenticated()));


                return null;
            }

        }.execute();

    }
}
