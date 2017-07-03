package com.apap.director.client.domain.interactor.register;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class CreateAccountInteractor extends BaseInteractor<AccountEntity, String> {

    private AccountRepository accountRepository;

    @Inject
    public CreateAccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Observable<AccountEntity> buildObservable(String name) {
        return accountRepository.createAccount(name);
    }
}
