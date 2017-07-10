package com.apap.director.client.presentation.ui.contact.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.contact.NewContactActivity;
import com.apap.director.client.presentation.ui.contact.di.module.NewContactContractModule;

import dagger.Component;

/**
 * Created by Adam Poterałowicz
 */

@Activity
@Component(modules = {NewContactContractModule.class}, dependencies = {MainComponent.class})
public interface NewContactComponent {
    void inject(NewContactActivity newContactActivity);
}
