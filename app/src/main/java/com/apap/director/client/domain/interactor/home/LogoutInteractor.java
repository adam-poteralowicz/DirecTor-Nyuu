package com.apap.director.client.domain.interactor.home;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class LogoutInteractor extends BaseInteractor<AccountModel, AccountModel> {

    private AccountRepository accountRepository;

    @Inject
    public LogoutInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    protected Observable<AccountModel> buildObservable(AccountModel accountModel) {
        return accountRepository.logOut(accountModel);
    }
}
