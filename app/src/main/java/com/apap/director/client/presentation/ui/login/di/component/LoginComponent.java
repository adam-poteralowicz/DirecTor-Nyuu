package com.apap.director.client.presentation.ui.login.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.module.ManagerModule;
import com.apap.director.client.presentation.di.module.RealmModule;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.login.LoginActivity;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;
import com.apap.director.client.presentation.ui.login.di.module.LoginContractModule;

import dagger.Component;

/**
 * Created by Alicja Michniewicz on 03/07/2017.
 */

@Activity
@Component(modules = {LoginContractModule.class}, dependencies = {MainComponent.class})
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
