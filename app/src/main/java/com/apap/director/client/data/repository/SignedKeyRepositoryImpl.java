package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.service.DbSignedKeyService;
import com.apap.director.client.domain.repository.SignedKeyRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class SignedKeyRepositoryImpl implements SignedKeyRepository {

    private DbSignedKeyService dbSignedKeyService;

    @Inject
    public SignedKeyRepositoryImpl(DbSignedKeyService dbSignedKeyService) {
        this.dbSignedKeyService = dbSignedKeyService;
    }


    @Override
    public Observable<Long> findNextId() {
        return Observable.just(dbSignedKeyService.findNextId());
    }
}
