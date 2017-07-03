package com.apap.director.client.presentation.ui.home.di.component;

import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.home.HomeActivity;
import com.apap.director.client.presentation.ui.home.di.module.HomeContractModule;

import dagger.Component;

/**
 * Created by Adam on 2017-07-03.
 */

@Activity
@Component(modules = {HomeContractModule.class}, dependencies = {MainComponent.class})
public interface HomeComponent {
    void inject(HomeActivity homeActivity);
}
