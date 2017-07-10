package com.apap.director.client.presentation.ui.login.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.login.LoginActivity;
import com.apap.director.client.presentation.ui.login.di.module.LoginContractModule;

import dagger.Component;

@Activity
@Component(modules = {LoginContractModule.class}, dependencies = {MainComponent.class})
public interface LoginContractComponent {
    void inject(LoginActivity loginActivity);
}
