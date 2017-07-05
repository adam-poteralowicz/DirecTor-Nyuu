package com.apap.director.client.presentation.ui.inbox.di;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.inbox.InboxFragment;
import com.apap.director.client.presentation.ui.inbox.di.module.InboxContractModule;

import dagger.Component;

/**
 * Created by Adam on 2017-07-05.
 */

@Activity
@Component(modules = {InboxContractModule.class}, dependencies = {MainComponent.class})
public interface InboxComponent {

    void inject(InboxFragment inboxFragment);
}
