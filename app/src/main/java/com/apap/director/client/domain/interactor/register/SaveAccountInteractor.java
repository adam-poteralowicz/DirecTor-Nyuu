package com.apap.director.client.domain.interactor.register;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class SaveAccountInteractor extends BaseInteractor<AccountModel, AccountModel> {

    private AccountRepository accountRepository;

    public SaveAccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Observable<AccountModel> buildObservable(AccountModel accountModel) {
        return accountRepository.saveAccount(accountModel);
    }
}
