package com.apap.director.client.presentation.di.module;

import android.content.Context;

import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.data.manager.MessageManager;
import com.apap.director.client.data.net.service.MessageAction;

import com.apap.director.client.data.store.IdentityKeyStoreImpl;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.data.store.SessionStoreImpl;
import com.apap.director.client.data.store.SignedPreKeyStoreImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WebSocketModule {

    @Provides
    @Singleton
    MessageAction provideMessageAction(IdentityKeyStoreImpl identityKeyStore, SessionStoreImpl sessionStore, SignedPreKeyStoreImpl signedPreKeyStore, PreKeyStoreImpl preKeyStore, MessageManager messageManager, ContactManager contactManager, ConversationManager conversationManager, Context context) {
        return new MessageAction(preKeyStore, identityKeyStore, sessionStore, signedPreKeyStore, messageManager, contactManager, conversationManager);
    }

}
