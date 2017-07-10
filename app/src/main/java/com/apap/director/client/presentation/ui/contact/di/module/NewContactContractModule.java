package com.apap.director.client.presentation.ui.contact.di.module;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.contact.contract.NewContactContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam Poterałowicz
 */

@Module
public class NewContactContractModule {

    private NewContactContract.View view;

    public NewContactContractModule(NewContactContract.View view) {
        this.view = view;
    }

    @Provides
    @Activity
    public NewContactContract.View provideView() {
        return view;
    }
}
