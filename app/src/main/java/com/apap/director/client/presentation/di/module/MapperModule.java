package com.apap.director.client.presentation.di.module;

import com.apap.director.client.data.db.mapper.AccountMapper;
import com.apap.director.client.data.db.mapper.ContactKeyMapper;
import com.apap.director.client.data.db.mapper.ContactMapper;
import com.apap.director.client.data.db.mapper.ConversationMapper;
import com.apap.director.client.data.db.mapper.MessageMapper;
import com.apap.director.client.data.db.mapper.OneTimeKeyMapper;
import com.apap.director.client.data.db.mapper.SessionMapper;
import com.apap.director.client.data.db.mapper.SignedKeyMapper;
import com.apap.director.client.data.net.mapper.MessageTOMapper;
import com.apap.director.client.data.net.mapper.OneTimeKeyTOMapper;
import com.apap.director.client.data.net.mapper.SignedKeyTOMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam Poterałowicz
 */

@Module
public class MapperModule {

    /* Entity 2 Model mappers */
    
    @Provides
    @Singleton
    MessageMapper provideMessageMapper(MessageMapper messageMapper) {
        return messageMapper;
    }

    @Provides
    @Singleton
    ContactMapper provideContactMapper(ContactMapper contactMapper) {
        return contactMapper;
    }

    @Provides
    @Singleton
    ConversationMapper provideConversationMapper(ConversationMapper conversationMapper) {
        return conversationMapper;
    }

    @Provides
    @Singleton
    SessionMapper provideSessionMapper(SessionMapper sessionMapper) {
        return sessionMapper;
    }

    @Provides
    @Singleton
    AccountMapper provideAccountMapper(AccountMapper accountMapper) {
        return accountMapper;
    }

    @Provides
    @Singleton
    ContactKeyMapper provideContactKeyMapper(ContactKeyMapper contactKeyMapper) {
        return contactKeyMapper;
    }

    @Provides
    @Singleton
    OneTimeKeyMapper provideOneTimeKeyMapper(OneTimeKeyMapper oneTimeKeyMapper) {
        return oneTimeKeyMapper;
    }

    @Provides
    @Singleton
    SignedKeyMapper provideSignedKeyMapper(SignedKeyMapper signedKeyMapper) {
        return signedKeyMapper;
    }

    /* Transactional Object 2 Model mappers */


    @Provides
    @Singleton
    MessageTOMapper provideMessageTOMapper(MessageTOMapper messageTOMapper) {
        return messageTOMapper;
    }

    @Provides
    @Singleton
    OneTimeKeyTOMapper provideOneTimeKeyTOMapper(OneTimeKeyTOMapper oneTimeKeyTOMapper) {
        return oneTimeKeyTOMapper;
    }

    @Provides
    @Singleton
    SignedKeyTOMapper provideSignedKeyTOMapper(SignedKeyTOMapper signedKeyTOMapper) {
        return signedKeyTOMapper;
    }
}
