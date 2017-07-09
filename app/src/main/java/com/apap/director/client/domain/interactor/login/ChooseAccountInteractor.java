package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-09.
 */

public class ChooseAccountInteractor extends BaseInteractor<AccountModel, AccountModel> {

    private AccountRepository accountRepository;

    @Inject
    public ChooseAccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    protected Observable<AccountModel> buildObservable(AccountModel accountModel) {
        return accountRepository.chooseAccount(accountModel);
    }
}
