package com.apap.director.client.presentation.ui.inbox.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.inbox.InboxFragment;
import com.apap.director.client.presentation.ui.inbox.di.module.InboxContractModule;

import dagger.Component;

/**
 * Created by Adam Poterałowicz
 */

@Activity
@Component(modules = {InboxContractModule.class}, dependencies = {MainComponent.class})
public interface InboxComponent {

    void inject(InboxFragment inboxFragment);
}
