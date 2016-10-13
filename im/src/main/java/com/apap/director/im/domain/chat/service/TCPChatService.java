package com.apap.director.im.domain.chat.service;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

public class TCPChatService extends AbstractChatService {

    public TCPChatService() {
    }

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

                XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
                connection = new XMPPTCPConnection(builder.setServiceName(host).setPort(port).build());

                try {
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
                chatManager.addChatListener(chatEventListener);

                Log.v("HAI/Async", String.valueOf(connection.isConnected()));
                return null;
            }
        }.execute();

    }


}
