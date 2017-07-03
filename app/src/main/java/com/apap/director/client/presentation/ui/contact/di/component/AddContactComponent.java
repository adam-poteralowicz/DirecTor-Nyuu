package com.apap.director.client.presentation.ui.contact.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.contact.AddContactActivity;
import com.apap.director.client.presentation.ui.contact.di.module.AddContactContractModule;

import dagger.Component;

/**
 * Created by Adam on 2017-07-03.
 */

@Activity
@Component(modules = {AddContactContractModule.class}, dependencies = {MainComponent.class})
public interface AddContactComponent {
    void inject(AddContactActivity addContactActivity);
}
