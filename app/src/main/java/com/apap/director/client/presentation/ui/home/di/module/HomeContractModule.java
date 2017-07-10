package com.apap.director.client.presentation.ui.home.di.module;

import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.home.contract.HomeContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam Poterałowicz
 */

@Module
public class HomeContractModule {

    private HomeContract.View view;

    public HomeContractModule(HomeContract.View view) { this.view = view; }

    @Provides
    @Activity
    public HomeContract.View provideView() { return view; }
}
