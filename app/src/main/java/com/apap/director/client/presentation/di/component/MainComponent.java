package com.apap.director.client.presentation.di.component;

import android.content.Context;

import com.apap.director.client.App;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.net.rest.service.UserService;
import com.apap.director.client.data.store.IdentityKeyStoreImpl;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.presentation.di.module.ContextModule;
import com.apap.director.client.presentation.di.module.ManagerModule;
import com.apap.director.client.presentation.di.module.RealmModule;
import com.apap.director.client.presentation.ui.contact.AddContactActivity;
import com.apap.director.client.presentation.ui.home.HomeActivity;
import com.apap.director.client.presentation.ui.login.LoginActivity;
import com.apap.director.client.presentation.ui.register.NewAccountActivity;
import com.apap.director.client.presentation.ui.contact.NewContactActivity;
import com.apap.director.client.presentation.ui.message.NewMsgActivity;
import com.apap.director.client.presentation.ui.contact.SingleContactActivity;
import com.apap.director.client.presentation.ui.contact.ContactsFragment;
import com.apap.director.client.presentation.ui.inbox.InboxFragment;
import com.apap.director.client.presentation.di.module.WebSocketModule;
import com.apap.director.client.data.net.service.MessageAction;
import com.apap.director.client.presentation.ui.net.di.module.NetModule;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.store.SessionStoreImpl;
import com.apap.director.client.data.store.SignedPreKeyStoreImpl;
import com.apap.director.client.presentation.di.module.SignalModule;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

@Singleton
@Component(modules = {ManagerModule.class,
        RealmModule.class,
        NetModule.class,
        SignalModule.class,
        WebSocketModule.class,
        ContextModule.class })
public interface MainComponent {

    void inject(AddContactActivity addContactActivity);

    void inject(ContactsFragment contactsFragment);

    void inject(SingleContactActivity singleContactActivity);

    void inject(InboxFragment inboxFragment);

    void inject(NewMsgActivity newMsgActivity);

    void inject(NewAccountActivity newAccountActivity);

    void inject(HomeActivity homeActivity);

    void inject(App app);

    void inject(NewContactActivity newContactActivity);

    MessageAction getMessageAction();

    KeyService getKeyService();

    IdentityKeyStoreImpl getDirectorIdentityKeyStore();

    PreKeyStoreImpl getDirectorPreKeyStore();

    SignedPreKeyStoreImpl getDirectorSignedPreKeyStore();

    SessionStoreImpl getDirectorSessionStore();

    AccountManager accountManager();

    UserService userService();

    Realm realm();

    Context context();
}
