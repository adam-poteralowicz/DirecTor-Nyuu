package com.apap.director.client.data.repository;

import com.apap.director.client.domain.repository.OneTimeKeyRepository;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class OneTimeKeyRepositoryImpl implements OneTimeKeyRepository {
    @Override
    public Observable<Long> findNextId() {
        return null;
    }
}
