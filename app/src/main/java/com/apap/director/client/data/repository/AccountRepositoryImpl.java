package com.apap.director.client.data.repository;

import android.util.Base64;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.SessionEntity;
import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.ByteUtil;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.realm.RealmList;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public class AccountRepositoryImpl implements AccountRepository {

    private AccountStore accountStore;
    private RestAccountService restAccountService;

    @Inject
    public AccountRepositoryImpl(AccountStore accountStore, RestAccountService restAccountService) {
        this.accountStore = accountStore;
        this.restAccountService = restAccountService;
    }

    @Override
    public Observable<List<AccountEntity>> getAccountList() {
        return Observable.just(accountStore.getAccountList());
    }

    @Override
    public Observable<AccountEntity> getActiveAccount() {
        return Observable.just(accountStore.getActiveAccount());
    }

    @Override
    public Observable<String> getCode(AccountModel account) {
        return null;
    }

    @Override
    public Observable<ResponseBody> signUp(AccountModel account) {
        return restAccountService.signUp(account.getKeyBase64());
    }

    @Override
    public Observable<Integer> findLastSignedKeyId(AccountModel account) {
        return Observable.just(new Integer(accountStore.findLastSignedKeyId(account.getKeyBase64())));
    }

    @Override
    public Observable<Integer> findLastOneTimeKeyId(AccountModel account) {
        return Observable.just(accountStore.findLastOneTimeKeyId(account.getKeyBase64()));
    }

}
