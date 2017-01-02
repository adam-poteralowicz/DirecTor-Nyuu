package com.apap.director.client.component;

import com.apap.director.client.activity.AddContactActivity;
import com.apap.director.client.activity.LoginActivity;
import com.apap.director.client.activity.NewMsgActivity;
import com.apap.director.client.activity.SingleContactActivity;
import com.apap.director.client.fragment.ContactsFragment;
import com.apap.director.client.fragment.InboxFragment;
import com.apap.director.db.account.AccountModule;
import com.apap.director.db.dao.module.DaoModule;
import com.apap.director.db.rest.module.RestModule;
import com.apap.director.im.signal.module.SignalModule;
import com.apap.director.im.websocket.module.WebSocketModule;

import org.java_websocket.WebSocket;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AccountModule.class, DaoModule.class, RestModule.class, SignalModule.class, WebSocketModule.class})
public interface MainComponent {

    void inject(AddContactActivity addContactActivity);
    void inject(ContactsFragment contactsFragment);
    void inject(SingleContactActivity singleContactActivity);
    void inject(InboxFragment inboxFragment);
    void inject(NewMsgActivity newMsgActivity);
    void inject(LoginActivity loginActivity);

}
