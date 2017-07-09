package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class GetAccountListInteractor extends BaseInteractor<List<AccountModel>, Void> {

    private AccountRepository accountRepository;

    @Inject
    public GetAccountListInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Observable<List<AccountModel>> buildObservable(Void unused) {
        return accountRepository.getAccountList();
    }

}
