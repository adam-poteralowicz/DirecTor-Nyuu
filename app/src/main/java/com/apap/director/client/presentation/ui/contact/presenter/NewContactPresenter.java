package com.apap.director.client.presentation.ui.contact.presenter;

import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.NewContactContract;

import javax.inject.Inject;

/**
 * Created by Adam on 2017-07-03.
 */

public class NewContactPresenter implements BasePresenter, NewContactContract.Presenter {

    @Inject
    ContactManager contactManager;

    private NewContactContract.View view;

    @Inject
    NewContactPresenter(NewContactContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void addContact(String name, String publicKey) {
        if (name.length() == 0) {
            view.showSnackbar("Type a valid name");
        } else {
            contactManager.addContact(name, publicKey);
        }
    }

    @Override
    public void addConversation(String contactName) {
        view.addConversation(contactName);
    }
}
