package com.apap.director.client.domain.repository;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public interface OneTimeKeyRepository {

    Observable<Long> findNextId();
}
