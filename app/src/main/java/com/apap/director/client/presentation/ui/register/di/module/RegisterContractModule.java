package com.apap.director.client.presentation.ui.register.di.module;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.register.contract.RegisterContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alicja Michniewicz
 */

@Module
public class RegisterContractModule {

    private RegisterContract.View view;

    public RegisterContractModule(RegisterContract.View view) {
        this.view = view;
    }

    @Provides
    @Activity
    public RegisterContract.View provideView() {
        return view;
    }
}
