package com.apap.director.im.domain.chat.event;

import android.util.Log;

import com.apap.director.im.domain.message.event.MessageEventListener;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;

import javax.inject.Inject;

public class ChatEventListener implements ChatManagerListener {

    @Inject
    MessageEventListener listener;

    public void chatCreated(Chat chat, boolean isLocal) {
        chat.addMessageListener(listener);
        Log.i("ChatEventListener", "Chat with "+chat.getParticipant()+" created");
    }
}
