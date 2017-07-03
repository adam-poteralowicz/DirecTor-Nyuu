package com.apap.director.client.presentation.ui.contact.presenter;

import android.content.Context;
import android.widget.Toast;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.NewContactContract;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Adam on 2017-07-03.
 */

public class NewContactPresenter implements BasePresenter, NewContactContract.Presenter {

    @Inject
    ContactManager contactManager;

    @Inject
    ConversationManager conversationManager;

    private NewContactContract.View view;

    @Inject
    NewContactPresenter(NewContactContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void addContact(String name, String publicKey, Context context) {
        if (name.length() == 0) {
            Toast.makeText(context, "Type a valid name", Toast.LENGTH_SHORT).show();
        } else {
            contactManager.addContact(name, publicKey);
        }
    }

    @Override
    public void addConversation(String contactName) {
        Realm realm = Realm.getDefaultInstance();
        ContactEntity contact = realm.where(ContactEntity.class).equalTo("name", contactName).findFirst();
        conversationManager.addConversation(contact);
        realm.close();
    }
}
