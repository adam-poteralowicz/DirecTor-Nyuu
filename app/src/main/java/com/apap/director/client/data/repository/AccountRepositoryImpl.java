package com.apap.director.client.data.repository;

import android.util.Base64;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.SessionEntity;
import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.data.net.rest.service.RestAccountService;
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
import io.realm.RealmList;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public class AccountRepositoryImpl implements AccountRepository {



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
    public Observable<ResponseBody> signUp(String userId) {
        return restAccountService.signUp(userId);
    }
    
}
