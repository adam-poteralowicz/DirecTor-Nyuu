package com.apap.director.client.presentation.ui.register.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.register.NewAccountActivity;
import com.apap.director.client.presentation.ui.register.di.module.RegisterContractModule;

import dagger.Component;

/**
 * Created by Alicja Michniewicz
 */

@Activity
@Component(modules = {RegisterContractModule.class}, dependencies = {MainComponent.class})
public interface RegisterContractComponent {

    void inject(NewAccountActivity newAccountActivity);
}
