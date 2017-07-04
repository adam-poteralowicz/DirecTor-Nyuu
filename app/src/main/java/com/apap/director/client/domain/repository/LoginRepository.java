package com.apap.director.client.domain.repository;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public interface LoginRepository {

    Observable<String> getCode(String userId);

}
