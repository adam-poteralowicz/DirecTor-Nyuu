package com.apap.director.client.domain.repository;

import android.accounts.Account;

import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.SignedKeyModel;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public interface SignedKeyRepository {

    Observable<SignedKeyModel> findLastestSignedKey(AccountModel owner);
}
