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
    MessageMapper provideMessageMapper(ConversationMapper conversationMapper) {
        return new MessageMapper(conversationMapper);
    }

    @Provides
    @Singleton
    ContactMapper provideContactMapper(AccountMapper accountMapper, ContactKeyMapper contactKeyMapper) {
        return new ContactMapper(contactKeyMapper, accountMapper);
    }

    @Provides
    @Singleton
    ConversationMapper provideConversationMapper(ContactMapper contactMapper) {
        return new ConversationMapper(contactMapper);
    }

    @Provides
    @Singleton
    SessionMapper provideSessionMapper(AccountMapper accountMapper) {
        return new SessionMapper(accountMapper);
    }

    @Provides
    @Singleton
    AccountMapper provideAccountMapper(OneTimeKeyMapper oneTimeKeyMapper, SignedKeyMapper signedKeyMapper) {
        return new AccountMapper(oneTimeKeyMapper, signedKeyMapper);
    }

    @Provides
    @Singleton
    ContactKeyMapper provideContactKeyMapper() {
        return new ContactKeyMapper();
    }

    @Provides
    @Singleton
    OneTimeKeyMapper provideOneTimeKeyMapper() {
        return new OneTimeKeyMapper();
    }

    @Provides
    @Singleton
    SignedKeyMapper provideSignedKeyMapper() {
        return new SignedKeyMapper();
    }

    /* Transactional Object 2 Model mappers */


    @Provides
    @Singleton
    MessageTOMapper provideMessageTOMapper() {
        return new MessageTOMapper();
    }

    @Provides
    @Singleton
    OneTimeKeyTOMapper provideOneTimeKeyTOMapper() {
        return new OneTimeKeyTOMapper();
    }

    @Provides
    @Singleton
    SignedKeyTOMapper provideSignedKeyTOMapper() {
        return new SignedKeyTOMapper();
    }
}
