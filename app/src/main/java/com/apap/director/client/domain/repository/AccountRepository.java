package com.apap.director.client.domain.repository;

import com.apap.director.client.data.db.entity.AccountEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public interface AccountRepository {

    Observable<List<AccountEntity>> getAccountList();

}
