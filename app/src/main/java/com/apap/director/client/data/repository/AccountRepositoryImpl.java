package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.mapper.AccountMapper;
import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.data.net.mapper.OneTimeKeyTOMapper;
import com.apap.director.client.data.net.mapper.SignedKeyTOMapper;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import java.util.ArrayList;
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
    private KeyService keyService;
    private OneTimeKeyTOMapper otkTOmapper;
    private SignedKeyTOMapper skTOmapper;

    @Inject
    public AccountRepositoryImpl(AccountStore accountStore, RestAccountService restAccountService, AccountMapper accountMapper, OneTimeKeyTOMapper otkTOmapper, SignedKeyTOMapper skTOmapper, KeyService keyService) {
        this.accountStore = accountStore;
        this.restAccountService = restAccountService;
        this.accountMapper = accountMapper;
        this.otkTOmapper = otkTOmapper;
        this.skTOmapper = skTOmapper;
        this.keyService = keyService;
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

    @Override
    public Observable<Boolean> deleteAccount(AccountModel account) {
        AccountEntity entity = accountMapper.mapToEntity(account);
        accountStore.deleteAccount(entity);
        return Observable.just(!entity.isValid());
    }

    @Override
    public Observable<ResponseBody> postOneTimeKeys(AccountModel account) {
        List<OneTimeKeyTO> keyTOs = new ArrayList<>();
        for (int i = 0; i < account.getOneTimeKeys().size(); i++) {
            keyTOs.add(otkTOmapper.mapToTO(account.getOneTimeKeys().get(i)));
        }
        return keyService.postOneTimeKeys(keyTOs, account.getCookie());
    }

    @Override
    public Observable<ResponseBody> postSignedKeys(AccountModel account) {
        SignedKeyTO keyTO = skTOmapper.mapToTO(account.getSignedKey());
        keyTO.setSignatureBase64(accountStore.getSignedKeySignature(accountMapper.mapToEntity(account)));
        return keyService.postSignedKeys(keyTO, account.getCookie());
    }

    @Override
    public Observable<AccountModel> logOut(AccountModel account) {
        AccountEntity entity = accountMapper.mapToEntity(account);
        accountStore.logOut(entity);
        return Observable.just(accountMapper.mapToModel(entity));
    }

}
