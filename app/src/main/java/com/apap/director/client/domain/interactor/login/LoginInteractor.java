package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.LoginRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class LoginInteractor extends BaseInteractor<AccountModel, String> {

    private LoginRepository loginRepository;

    @Inject
    public LoginInteractor(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public Observable<AccountModel> buildObservable(String code) {
        return null;
    }
}
