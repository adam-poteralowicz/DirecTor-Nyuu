package com.apap.director.client.domain.repository;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.model.AccountModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public interface AccountRepository {

    Observable<List<AccountEntity>> getAccountList();

    Observable<AccountEntity> getActiveAccount();

    Observable<AccountEntity> getAccount(String name);

    Observable<ResponseBody> signUp(AccountModel account);

    Observable<Integer> findLastSignedKeyId(AccountModel account);

    Observable<Integer> findLastOneTimeKeyId(AccountModel account);

    Observable<AccountModel> saveAccount(AccountModel account);
}
