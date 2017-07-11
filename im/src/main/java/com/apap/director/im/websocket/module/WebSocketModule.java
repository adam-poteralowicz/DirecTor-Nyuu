package com.apap.director.im.websocket.module;

import android.content.Context;

import com.apap.director.im.websocket.service.MessageAction;
import com.apap.director.manager.ContactManager;
import com.apap.director.manager.ConversationManager;
import com.apap.director.manager.MessageManager;
import com.apap.director.signal.DirectorIdentityKeyStore;
import com.apap.director.signal.DirectorPreKeyStore;
import com.apap.director.signal.DirectorSessionStore;
import com.apap.director.signal.DirectorSignedPreKeyStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WebSocketModule {

    private Context context;

    public WebSocketModule(Context context) {

    }

    @Provides
    @Singleton
    public MessageAction provideMessageAction(DirectorIdentityKeyStore identityKeyStore, DirectorSessionStore sessionStore, DirectorSignedPreKeyStore signedPreKeyStore, DirectorPreKeyStore preKeyStore, MessageManager messageManager, ContactManager contactManager, ConversationManager conversationManager) {
        return new MessageAction(preKeyStore, identityKeyStore, sessionStore, signedPreKeyStore, messageManager);
    }

}
