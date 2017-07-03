package com.apap.director.client.presentation.di.module;

import com.apap.director.client.data.repository.AccountRepositoryImpl;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alicja Michniewicz
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    AccountRepository provideAccountRepository(AccountRepositoryImpl accountRepository) {
        return accountRepository;
    }

}
