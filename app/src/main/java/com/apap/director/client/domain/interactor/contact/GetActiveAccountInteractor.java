package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-04.
 */

public class GetActiveAccountInteractor extends BaseInteractor<AccountEntity, Void> {

    private AccountRepository accountRepository;

    @Inject
    public GetActiveAccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Observable<AccountEntity> buildObservable(Void aVoid) {
        return accountRepository.getActiveAccount();
    }
}
