package com.apap.director.client.presentation.ui.contact.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.contact.ContactsFragment;
import com.apap.director.client.presentation.ui.contact.di.module.ContactsContractModule;

import dagger.Component;

/**
 * Created by Adam Poterałowicz
 */

@Activity
@Component(modules = {ContactsContractModule.class}, dependencies = {MainComponent.class})
public interface ContactsComponent {
    void inject(ContactsFragment contactsFragment);
}
