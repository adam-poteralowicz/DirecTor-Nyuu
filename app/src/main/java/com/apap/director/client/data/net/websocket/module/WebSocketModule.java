package com.apap.director.client.data.net.websocket.module;

import android.content.Context;

import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.data.manager.MessageManager;
import com.apap.director.client.data.net.websocket.service.MessageAction;

import com.apap.director.client.data.store.DirectorIdentityKeyStore;
import com.apap.director.client.data.store.DirectorPreKeyStore;
import com.apap.director.client.data.store.DirectorSessionStore;
import com.apap.director.client.data.store.DirectorSignedPreKeyStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WebSocketModule {

    private Context context;

    public WebSocketModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    MessageAction provideMessageAction(DirectorIdentityKeyStore identityKeyStore, DirectorSessionStore sessionStore, DirectorSignedPreKeyStore signedPreKeyStore, DirectorPreKeyStore preKeyStore, MessageManager messageManager, ContactManager contactManager, ConversationManager conversationManager) {
        return new MessageAction(preKeyStore, identityKeyStore, sessionStore, signedPreKeyStore, messageManager, contactManager, conversationManager);
    }

}
