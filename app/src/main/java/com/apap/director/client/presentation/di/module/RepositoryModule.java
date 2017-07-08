package com.apap.director.client.presentation.di.module;

import com.apap.director.client.data.repository.AccountRepositoryImpl;
import com.apap.director.client.data.repository.ContactRepositoryImpl;
import com.apap.director.client.data.repository.ConversationRepositoryImpl;
import com.apap.director.client.data.repository.MessageRepositoryImpl;
import com.apap.director.client.data.repository.SignedKeyRepositoryImpl;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.ContactRepository;
import com.apap.director.client.domain.repository.ConversationRepository;
import com.apap.director.client.domain.repository.MessageRepository;
import com.apap.director.client.domain.repository.SignedKeyRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alicja Michniewicz
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    AccountRepository provideAccountRepository(AccountRepositoryImpl accountRepository) {
        return accountRepository;
    }

    @Provides
    @Singleton
    MessageRepository provideMessageRepository(MessageRepositoryImpl messageRepository) {
        return messageRepository;
    }

    @Provides
    @Singleton
    ContactRepository provideContactRepository(ContactRepositoryImpl contactRepository) {
        return contactRepository;
    }

    @Provides
    @Singleton
    ConversationRepository provideConversationRepository(ConversationRepositoryImpl conversationRepository) {
        return conversationRepository;
    }

    @Provides
    @Singleton
    SignedKeyRepository provideSignedKeyRepository(SignedKeyRepositoryImpl signedKeyRepository) {
        return signedKeyRepository;
    }
}
