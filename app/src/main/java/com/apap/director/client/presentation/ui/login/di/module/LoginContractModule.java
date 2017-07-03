package com.apap.director.client.presentation.ui.login.di.module;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alicja Michniewicz on 03/07/2017.
 */

@Module
public class LoginContractModule {

    private LoginContract.View view;

    public LoginContractModule(LoginContract.View view) {
        this.view = view;
    }

    @Provides
    @Activity
    public LoginContract.View provideView() {
        return view;
    }
}
