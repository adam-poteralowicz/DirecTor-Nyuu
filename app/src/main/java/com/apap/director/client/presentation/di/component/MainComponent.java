package com.apap.director.client.presentation.di.component;

import com.apap.director.client.App;
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
import com.apap.director.client.data.net.websocket.module.WebSocketModule;
import com.apap.director.client.data.net.websocket.service.MessageAction;
import com.apap.director.client.data.net.rest.module.RestModule;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.store.DirectorIdentityKeyStore;
import com.apap.director.client.data.store.DirectorPreKeyStore;
import com.apap.director.client.data.store.DirectorSessionStore;
import com.apap.director.client.data.store.DirectorSignedPreKeyStore;
import com.apap.director.client.data.store.module.SignalModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ManagerModule.class, RealmModule.class, RestModule.class, SignalModule.class, WebSocketModule.class})
public interface MainComponent {

    void inject(AddContactActivity addContactActivity);

    void inject(ContactsFragment contactsFragment);

    void inject(SingleContactActivity singleContactActivity);

    void inject(InboxFragment inboxFragment);

    void inject(NewMsgActivity newMsgActivity);

    void inject(LoginActivity loginActivity);

    void inject(NewAccountActivity newAccountActivity);

    void inject(HomeActivity homeActivity);

    void inject(App app);

    void inject(NewContactActivity newContactActivity);

    MessageAction getMessageAction();

    KeyService getKeyService();

    DirectorIdentityKeyStore getDirectorIdentityKeyStore();

    DirectorPreKeyStore getDirectorPreKeyStore();

    DirectorSignedPreKeyStore getDirectorSignedPreKeyStore();

    DirectorSessionStore getDirectorSessionStore();
}
