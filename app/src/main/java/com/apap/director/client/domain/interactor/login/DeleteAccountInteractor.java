package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class DeleteAccountInteractor extends BaseInteractor<Boolean, AccountModel> {

    private AccountRepository accountRepository;

    @Inject
    public DeleteAccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    protected Observable<Boolean> buildObservable(AccountModel accountModel) {
        return accountRepository.deleteAccount(accountModel);
    }
}
