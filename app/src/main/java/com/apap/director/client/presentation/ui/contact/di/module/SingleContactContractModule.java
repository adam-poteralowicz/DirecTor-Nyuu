package com.apap.director.client.presentation.ui.contact.di.module;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.contact.contract.SingleContactContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam Poterałowicz
 */


@Module
public class SingleContactContractModule {

    private SingleContactContract.View view;

    public SingleContactContractModule(SingleContactContract.View view) {
        this.view = view;
    }

    @Provides
    @Activity
    public SingleContactContract.View provideView() {
        return view;
    }
}
