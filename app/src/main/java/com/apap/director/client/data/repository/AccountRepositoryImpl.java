package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.service.DbAccountService;
import com.apap.director.client.domain.repository.AccountRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class AccountRepositoryImpl implements AccountRepository {

    private DbAccountService dbAccountService;

    @Inject
    public AccountRepositoryImpl(DbAccountService dbAccountService) {
        this.dbAccountService = dbAccountService;
    }

    @Override
    public Observable<List<AccountEntity>> getAccountList() {
        return Observable.just(dbAccountService.getAccountList());
    }
}
