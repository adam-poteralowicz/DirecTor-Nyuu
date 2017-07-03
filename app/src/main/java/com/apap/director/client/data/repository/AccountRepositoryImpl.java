package com.apap.director.client.data.repository;

import android.util.Base64;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.domain.repository.AccountRepository;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.util.ByteUtil;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public class AccountRepositoryImpl implements AccountRepository {

    private final static int KEY_LENGTH = 32;
    private final static int TYPE_LENGTH = 1;

    private AccountStore accountStore;
    private RestAccountService restAccountService;
    private IdentityKeyStore identityKeyStore;

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
    public Observable<String> getCode(String userId) {
        return restAccountService.requestCode(userId);
    }

    @Override
    public Observable<ResponseBody> signUp(String userId) {
        return restAccountService.signUp(userId);
    }

    @Override
    public Observable<AccountEntity> createAccount(String name) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName(name);

        return Observable.just(preconfigureAccount(accountEntity));
    }

    private AccountEntity preconfigureAccount(AccountEntity accountEntity) {
        IdentityKeyPair keyPair = KeyHelper.generateIdentityKeyPair();

        accountEntity.setKeyPair(keyPair.serialize());
        accountEntity.setRegistrationId(KeyHelper.generateRegistrationId(false));
        accountEntity.setKeyBase64(convertToBase64(keyPair.getPublicKey()));

        return accountEntity;
    }

    private String convertToBase64(IdentityKey key) {
        byte[][] typeAndKey = ByteUtil.split(key.serialize(), TYPE_LENGTH, KEY_LENGTH);
        return Base64.encodeToString(typeAndKey[1], Base64.URL_SAFE | Base64.NO_WRAP);
    }
}
