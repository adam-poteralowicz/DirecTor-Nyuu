package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Adam Poterałowicz
 */

public class PostSignedKeysInteractor extends BaseInteractor<ResponseBody, AccountModel> {

    private AccountRepository accountRepository;

    @Inject
    public  PostSignedKeysInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    protected Observable<ResponseBody> buildObservable(AccountModel accountModel) {
        return accountRepository.postSignedKeys(accountModel);
    }
}
