package com.apap.director.client.domain.repository;

import com.apap.director.client.data.db.entity.AccountEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public interface AccountRepository {

    Observable<List<AccountEntity>> getAccountList();
    
    Observable<ResponseBody> signUp(String userId);
}
