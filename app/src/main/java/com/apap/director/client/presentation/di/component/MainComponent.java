package com.apap.director.client.presentation.di.component;

import android.content.Context;

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
import com.apap.director.client.presentation.di.module.ContextModule;
import com.apap.director.client.presentation.di.module.ManagerModule;
import com.apap.director.client.presentation.di.module.NetModule;
import com.apap.director.client.presentation.di.module.RealmModule;
import com.apap.director.client.presentation.di.module.RepositoryModule;
import com.apap.director.client.presentation.di.module.SignalModule;
import com.apap.director.client.presentation.di.module.WebSocketModule;
import com.apap.director.client.presentation.ui.contact.ContactsFragment;
import com.apap.director.client.presentation.ui.inbox.InboxFragment;

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
        ContextModule.class})
public interface MainComponent {

    void inject(ContactsFragment contactsFragment);

    void inject(InboxFragment inboxFragment);

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

    Context context();

    MessageManager messageManager();
}
