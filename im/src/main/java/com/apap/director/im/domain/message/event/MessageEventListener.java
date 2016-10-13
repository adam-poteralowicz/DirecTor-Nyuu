package com.apap.director.im.domain.message.event;

import android.util.Log;

import com.apap.director.im.dao.model.DaoSession;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import javax.inject.Inject;

public class MessageEventListener implements ChatMessageListener {

    @Inject
    DaoSession daoSession;

    public void processMessage(Chat chat, Message message) {
        Log.v("MessageEvenListener", message.getFrom()+": "+message.getBody());
    }
}
