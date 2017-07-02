package com.apap.director.client.presentation.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alicja Michniewicz on 02/07/2017.
 */

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }
}
