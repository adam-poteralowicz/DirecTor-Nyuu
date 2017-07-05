package com.apap.director.client.data.repository;

import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.SignedKeyModel;
import com.apap.director.client.domain.repository.SignedKeyRepository;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class SignedKeyRepositoryImpl implements SignedKeyRepository {

    pr

    @Override
    public Observable<SignedKeyModel> findLastestSignedKey(AccountModel owner) {
        return null;
    }
}
