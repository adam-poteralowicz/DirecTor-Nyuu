package com.apap.director.client.domain.interactor.register;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public class RegisterAccountInteractor extends BaseInteractor<ResponseBody,AccountModel> {

    private AccountRepository accountRepository;

    @Inject
    public RegisterAccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Observable<ResponseBody> buildObservable(AccountModel user) {
        return accountRepository.signUp(user);
    }
}
