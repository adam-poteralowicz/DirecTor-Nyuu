package com.apap.director.client.presentation.ui.message.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.message.NewMsgActivity;
import com.apap.director.client.presentation.ui.message.di.module.NewMsgContractModule;

import dagger.Component;

/**
 * Created by Adam Poterałowicz
 */

@Activity
@Component(modules = {NewMsgContractModule.class}, dependencies = {MainComponent.class})
public interface NewMsgComponent {
    void inject(NewMsgActivity newMsgActivity);
}
