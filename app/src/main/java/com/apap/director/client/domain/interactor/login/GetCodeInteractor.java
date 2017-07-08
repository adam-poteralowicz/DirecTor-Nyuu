package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.LoginRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class GetCodeInteractor extends BaseInteractor<String,AccountModel> {

    private LoginRepository loginRepository;

    @Inject
    public GetCodeInteractor(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public Observable<String> buildObservable(AccountModel accountModel) {
        return loginRepository.getCode(accountModel.getKeyBase64());
    }
}
