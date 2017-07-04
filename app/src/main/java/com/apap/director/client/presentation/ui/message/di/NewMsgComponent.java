package com.apap.director.client.presentation.ui.message.di;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.message.NewMsgActivity;

import dagger.Component;

/**
 * Created by Adam on 2017-07-04.
 */

@Activity
@Component(modules = {NewMsgContractModule.class}, dependencies = {MainComponent.class})
public interface NewMsgComponent {
    void inject(NewMsgActivity newMsgActivity);
}
