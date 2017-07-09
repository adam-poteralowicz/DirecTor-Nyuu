package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.mapper.AccountMapper;
import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public class AccountRepositoryImpl implements AccountRepository {

    private AccountStore accountStore;
    private RestAccountService restAccountService;
    private AccountMapper accountMapper;

    @Inject
    public AccountRepositoryImpl(AccountStore accountStore, RestAccountService restAccountService, AccountMapper accountMapper) {
        this.accountStore = accountStore;
        this.restAccountService = restAccountService;
        this.accountMapper = accountMapper;
    }

    @Override
    public Observable<List<AccountModel>> getAccountList() {
        return Observable.just(accountMapper.mapToList(accountMapper, accountStore.getAccountList()));
    }

    @Override
    public Observable<AccountModel> getActiveAccount() {
        return Observable.just(accountMapper.mapToModel(accountStore.getActiveAccount()));
    }

    @Override
    public Observable<AccountModel> getAccount(String name) {
        return Observable.just(accountMapper.mapToModel(accountStore.findAccountByName(name)));
    }

    @Override
    public Observable<ResponseBody> signUp(AccountModel account) {
        return restAccountService.signUp(account.getKeyBase64());
    }

    @Override
    public Observable<Integer> findLastSignedKeyId(AccountModel account) {
        return Observable.just(accountStore.findLastSignedKeyId(account.getKeyBase64()));
    }

    @Override
    public Observable<Integer> findLastOneTimeKeyId(AccountModel account) {
        return Observable.just(accountStore.findLastOneTimeKeyId(account.getKeyBase64()));
    }

    @Override
    public Observable<AccountModel> saveAccount(AccountModel account) {
        AccountEntity entity = accountMapper.mapToEntity(account);
        accountStore.saveAccount(entity);
        return Observable.just(account);
    }

    @Override
    public Observable<AccountModel> chooseAccount(AccountModel account) {
        AccountEntity entity = accountMapper.mapToEntity(account);
        entity.setActive(true);
        accountStore.updateAccount(entity);
        return Observable.just(accountMapper.mapToModel(entity));
    }

}
