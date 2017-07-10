package com.apap.director.client.presentation.ui.inbox.di.module;

import com.apap.director.client.domain.repository.ConversationRepository;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.inbox.contract.InboxContract;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam Poterałowicz
 */

@Module
public class InboxContractModule {

    private InboxContract.View view;

    @Inject
    public InboxContractModule(InboxContract.View view) {
        this.view = view;
    }

    @Provides
    @Activity
    InboxContract.View provideView() {
        return view;
    }

}
