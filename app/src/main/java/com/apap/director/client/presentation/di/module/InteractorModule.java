package com.apap.director.client.presentation.di.module;

import com.apap.director.client.domain.interactor.contact.GetContactListInteractor;
import com.apap.director.client.domain.interactor.inbox.GetConversationListInteractor;
import com.apap.director.client.domain.interactor.login.GetAccountListInteractor;
import com.apap.director.client.domain.interactor.login.GetCodeInteractor;
import com.apap.director.client.domain.interactor.login.LoginInteractor;
import com.apap.director.client.domain.interactor.message.GetMessageListInteractor;
import com.apap.director.client.domain.interactor.account.CreateAccountInteractor;
import com.apap.director.client.domain.interactor.register.GenerateOneTimeKeysInteractor;
import com.apap.director.client.domain.interactor.register.GenerateSignedKeyInteractor;
import com.apap.director.client.domain.interactor.register.RegisterAccountInteractor;
import com.apap.director.client.domain.interactor.register.SaveAccountInteractor;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.ContactRepository;
import com.apap.director.client.domain.repository.ConversationRepository;
import com.apap.director.client.domain.repository.LoginRepository;
import com.apap.director.client.domain.repository.MessageRepository;
import com.apap.director.client.domain.repository.OneTimeKeyRepository;
import com.apap.director.client.domain.repository.SignedKeyRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alicja Michniewicz
 */

@Module
public class InteractorModule {

    @Singleton
    @Provides
    GetAccountListInteractor provideGetAccountListInteractor(AccountRepository accountRepository) {
        return new GetAccountListInteractor(accountRepository);
    }

    @Singleton
    @Provides
    GetContactListInteractor provideGetContactListInteractor(ContactRepository contactRepository) {
        return new GetContactListInteractor(contactRepository);
    }

    @Singleton
    @Provides
    GetConversationListInteractor provideGetConversationListInteractor(ConversationRepository conversationRepository) {
        return new GetConversationListInteractor(conversationRepository);
    }

    @Singleton
    @Provides
    GetAccountListInteractor provideAccountListInteractor(AccountRepository accountRepository) {
        return new GetAccountListInteractor(accountRepository);
    }

    @Singleton
    @Provides
    GetCodeInteractor provideGetCodeInteractor(LoginRepository loginRepository) {
        return new GetCodeInteractor(loginRepository);
    }

    @Singleton
    @Provides
    LoginInteractor provideLoginInteractor(LoginRepository loginRepository) {
        return new LoginInteractor(loginRepository);
    }

    @Singleton
    @Provides
    GetMessageListInteractor provideGetMessageListInteractor(MessageRepository messageRepository) {
        return new GetMessageListInteractor(messageRepository);
    }

    @Singleton
    @Provides
    CreateAccountInteractor provideCreateAccountInteractor(GenerateSignedKeyInteractor generateSignedKeyInteractor, GenerateOneTimeKeysInteractor generateOneTimeKeysInteractor) {
        return new CreateAccountInteractor(generateSignedKeyInteractor, generateOneTimeKeysInteractor);
    }

    @Singleton
    @Provides
    GenerateOneTimeKeysInteractor generateOneTimeKeysInteractor(AccountRepository accountRepository, OneTimeKeyRepository oneTimeKeyRepository) {
        return new GenerateOneTimeKeysInteractor(accountRepository, oneTimeKeyRepository);
    }

    @Singleton
    @Provides
    GenerateSignedKeyInteractor generateSignedKeyInteractor(AccountRepository accountRepository, SignedKeyRepository signedKeyRepository) {
        return new GenerateSignedKeyInteractor(accountRepository, signedKeyRepository);
    }

    @Singleton
    @Provides
    RegisterAccountInteractor registerAccountInteractor(AccountRepository accountRepository) {
        return new RegisterAccountInteractor(accountRepository);
    }

    @Singleton
    @Provides
    SaveAccountInteractor provideSaveAccountInteractor(AccountRepository accountRepository) {
        return new SaveAccountInteractor(accountRepository);
    }
}
