package com.apap.director.client.presentation.ui.contact.di.module;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.contact.contract.AddContactContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam on 2017-07-03.
 */

@Module
public class AddContactContractModule {

    private AddContactContract.View view;

    public AddContactContractModule(AddContactContract.View view) {
        this.view = view;
    }

    @Provides
    @Activity
    public AddContactContract.View provideView() {
        return view;
    }
}
