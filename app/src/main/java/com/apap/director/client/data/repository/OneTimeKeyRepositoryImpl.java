package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.service.DbOneTimeKeyService;
import com.apap.director.client.domain.repository.OneTimeKeyRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class OneTimeKeyRepositoryImpl implements OneTimeKeyRepository {

    private DbOneTimeKeyService dbOneTimeKeyService;

    @Inject
    public OneTimeKeyRepositoryImpl(DbOneTimeKeyService dbOneTimeKeyService) {
        this.dbOneTimeKeyService = dbOneTimeKeyService;
    }

    @Override
    public Observable<Long> findNextId() {
        return Observable.just(dbOneTimeKeyService.findNextId());
    }
}
