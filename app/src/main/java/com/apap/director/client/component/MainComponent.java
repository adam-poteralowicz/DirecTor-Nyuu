package com.apap.director.client.component;

import com.apap.director.client.App;
import com.apap.director.client.activity.AddContactActivity;
import com.apap.director.client.activity.AuthUserActivity;
import com.apap.director.client.activity.LoginActivity;
import com.apap.director.client.activity.NewAccountActivity;
import com.apap.director.client.activity.NewContactActivity;
import com.apap.director.client.activity.NewMsgActivity;
import com.apap.director.client.activity.SingleContactActivity;
import com.apap.director.client.fragment.ContactsFragment;
import com.apap.director.client.fragment.InboxFragment;
import com.apap.director.db.dao.module.DaoModule;
import com.apap.director.signal.DirectorIdentityKeyStore;
import com.apap.director.signal.DirectorPreKeyStore;
import com.apap.director.signal.DirectorSessionStore;
import com.apap.director.signal.DirectorSignedPreKeyStore;
import com.apap.director.signal.module.SignalModule;
import com.apap.director.im.websocket.module.WebSocketModule;
import com.apap.director.im.websocket.service.MessageAction;
import com.apap.director.manager.ManagerModule;
import com.apap.director.network.rest.module.RestModule;
import com.apap.director.network.rest.service.KeyService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ManagerModule.class, DaoModule.class, RestModule.class, SignalModule.class, WebSocketModule.class})
public interface MainComponent {

    void inject(AddContactActivity addContactActivity);
    void inject(ContactsFragment contactsFragment);
    void inject(SingleContactActivity singleContactActivity);
    void inject(InboxFragment inboxFragment);
    void inject(NewMsgActivity newMsgActivity);
    void inject(LoginActivity loginActivity);
    void inject(NewAccountActivity newAccountActivity);
    void inject(AuthUserActivity authUserActivity);
    void inject(App app);
    void inject(NewContactActivity newContactActivity);
    MessageAction getMessageAction();
    KeyService getKeyService();
    DirectorIdentityKeyStore getDirectorIdentityKeyStore();
    DirectorPreKeyStore getDirectorPreKeyStore();
    DirectorSignedPreKeyStore getDirectorSignedPreKeyStore();
    DirectorSessionStore getDirectorSessionStore();

}
