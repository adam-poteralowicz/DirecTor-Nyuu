package com.apap.director.client.presentation.ui.contact.di.module;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.contact.contract.ContactsContract;
import com.apap.director.client.presentation.ui.contact.presenter.ContactsPresenter;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam Poterałowicz
 */

@Module
public class ContactsContractModule {

    private ContactsContract.View view;
    private ContactsPresenter contactsPresenter;

    @Inject
    public ContactsContractModule(ContactsContract.View view, ContactsPresenter contactsPresenter) {
        this.view = view;
        this.contactsPresenter = contactsPresenter;
    }

    @Provides
    @Activity
    public ContactsContract.View provideView() {
        return view;
    }

    @Provides
    @Activity
    public ContactsPresenter provideContactsPresenter() {
        return contactsPresenter;
    }
}
