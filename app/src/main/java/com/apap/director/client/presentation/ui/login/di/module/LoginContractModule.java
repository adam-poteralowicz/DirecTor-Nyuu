package com.apap.director.client.presentation.ui.login.di.module;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginContractModule {

    private WeakReference<LoginContract.View> view;

    public LoginContractModule(WeakReference<LoginContract.View> view) {
        this.view = view;
    }

    @Provides
    @Activity
    public WeakReference<LoginContract.View> provideView() {
        return view;
    }
}
