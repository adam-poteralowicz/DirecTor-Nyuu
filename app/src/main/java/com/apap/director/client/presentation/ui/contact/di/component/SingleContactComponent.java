package com.apap.director.client.presentation.ui.contact.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.contact.SingleContactActivity;
import com.apap.director.client.presentation.ui.contact.di.module.SingleContactContractModule;

import dagger.Component;

/**
 * Created by Adam Poterałowicz
 */

@Activity
@Component(modules = {SingleContactContractModule.class}, dependencies = {MainComponent.class})
public interface SingleContactComponent {
    void inject(SingleContactActivity singleContactActivity);
}
