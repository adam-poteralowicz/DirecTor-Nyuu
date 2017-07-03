package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.repository.AccountRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Alicja Michniewicz
 */

public class GetAccountListInteractor extends BaseInteractor<List<AccountEntity>, Void> {

    private AccountRepository accountRepository;

    @Inject
    public GetAccountListInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Observable<List<AccountEntity>> buildObservable(Void unused) {
        return accountRepository.getAccountList();
    }

}
