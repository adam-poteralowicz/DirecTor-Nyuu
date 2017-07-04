package com.apap.director.client.presentation.ui.message.di;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.message.contract.NewMsgContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam on 2017-07-04.
 */

@Module
public class NewMsgContractModule {

    private NewMsgContract.View view;

    public NewMsgContractModule(NewMsgContract.View view) {
        this.view = view;
    }

    @Activity
    @Provides
    public NewMsgContract.View provideView() {
        return view;
    }
}
