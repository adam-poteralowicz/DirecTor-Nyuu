package com.apap.director.im.domain.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.apap.director.im.domain.chat.event.ChatEventListener;
import com.apap.director.im.domain.message.event.MessageEventListener;
import com.apap.director.im.util.SimpleBinder;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Ala on 12/10/2016.
 */
public abstract class AbstractChatService extends Service implements Chat {

    AbstractXMPPConnection connection;
    ConnectionConfiguration configuration;
    ChatManager chatManager;

    @Inject
    ChatEventListener chatEventListener;

    @Inject
    MessageEventListener messageEventListener;


    public void login(String username, String password) throws IOException, XMPPException, SmackException {
        connection.login(username, password);
    }

    public void connect(String host, String port) throws IOException, XMPPException, SmackException {
        connection.connect();

        chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addChatListener(chatEventListener);

    }

    public void sendMessage(String to, String body) throws SmackException.NotConnectedException {
        Message message = new Message(to, body);
        org.jivesoftware.smack.chat.Chat chat = chatManager.createChat(to);
        chat.sendMessage(message);

        //TODO: create chat, add it to specific contact, close on deletion
    }

    public void setPresence(Presence presence) throws SmackException.NotConnectedException {
        connection.sendStanza(presence);
    }

    public IBinder onBind(Intent intent) {
        return new SimpleBinder(this);
    }
}
