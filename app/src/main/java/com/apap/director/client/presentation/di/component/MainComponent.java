package com.apap.director.client.presentation.di.component;

import android.content.Context;

import com.apap.director.client.data.db.mapper.OneTimeKeyMapper;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.manager.MessageManager;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.data.net.service.MessageAction;
import com.apap.director.client.data.store.IdentityKeyStoreImpl;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.data.store.SessionStoreImpl;
import com.apap.director.client.data.store.SignedPreKeyStoreImpl;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.SignedKeyRepository;
import com.apap.director.client.presentation.di.module.ContextModule;
import com.apap.director.client.presentation.di.module.InteractorModule;
import com.apap.director.client.presentation.di.module.ManagerModule;
import com.apap.director.client.presentation.di.module.NetModule;
import com.apap.director.client.presentation.di.module.RealmModule;
import com.apap.director.client.presentation.di.module.RepositoryModule;
import com.apap.director.client.presentation.di.module.SignalModule;
import com.apap.director.client.presentation.di.module.WebSocketModule;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

@Singleton
@Component(modules = {ManagerModule.class,
        RealmModule.class,
        NetModule.class,
        SignalModule.class,
        WebSocketModule.class,
        RepositoryModule.class,
        NetModule.class,
        InteractorModule.class,
        ContextModule.class})
public interface MainComponent {

    MessageAction getMessageAction();

    KeyService getKeyService();

    IdentityKeyStoreImpl getDirectorIdentityKeyStore();

    PreKeyStoreImpl getDirectorPreKeyStore();

    SignedPreKeyStoreImpl getDirectorSignedPreKeyStore();

    SessionStoreImpl getDirectorSessionStore();

    AccountManager accountManager();

    RestAccountService userService();

    Realm realm();

    AccountRepository accountRepository();

    SignedKeyRepository signedKeyRepository();

    Context context();

    MessageManager messageManager();

    OneTimeKeyMapper oneTimeKeyMapper();
}
