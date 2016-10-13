package com.apap.director.client.component;

import com.apap.director.client.activity.AddContactActivity;
import com.apap.director.client.activity.NewMsgActivity;
import com.apap.director.client.activity.SingleContactActivity;
import com.apap.director.client.fragment.ContactsFragment;
import com.apap.director.client.fragment.InboxFragment;
import com.apap.director.im.dao.module.DaoModule;
import com.apap.director.im.domain.chat.service.TCPChatService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DaoModule.class})
public interface DaoComponent {
    void inject(AddContactActivity addContactActivity);
    void inject(ContactsFragment contactsFragment);
    void inject(SingleContactActivity singleContactActivity);
    void inject(InboxFragment inboxFragment);
    void inject(NewMsgActivity newMsgActivity);
}
