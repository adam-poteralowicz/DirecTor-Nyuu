package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class GetActiveAccountInteractor extends BaseInteractor<AccountModel, Void> {

    private AccountRepository accountRepository;

    @Inject
    public GetActiveAccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Observable<AccountModel> buildObservable(Void aVoid) {
        return accountRepository.getActiveAccount();
    }
}
