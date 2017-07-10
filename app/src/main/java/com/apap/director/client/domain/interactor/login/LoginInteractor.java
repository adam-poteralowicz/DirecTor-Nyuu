package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.data.net.rest.service.LoginDetails;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.LoginRepository;
import com.apap.director.client.domain.util.Base64Util;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public class LoginInteractor extends BaseInteractor<ResponseBody, AccountModel> {

    private LoginRepository loginRepository;
    private GetCodeInteractor getCodeInteractor;
    private SignCodeInteractor signCodeInteractor;

    @Inject
    public LoginInteractor(LoginRepository loginRepository, GetCodeInteractor getCodeInteractor, SignCodeInteractor signCodeInteractor) {
        this.loginRepository = loginRepository;
        this.getCodeInteractor = getCodeInteractor;
        this.signCodeInteractor = signCodeInteractor;
    }

    @Override
    protected Observable<ResponseBody> buildObservable(AccountModel accountModel) {

        return getCodeInteractor.execute(accountModel)
                .flatMap(code -> signCodeInteractor.execute(code))
                .flatMap(signedCode -> {
                    LoginDetails loginDetails = new LoginDetails(accountModel.getKeyBase64(), Base64Util.convertToBase64(signedCode.getBytes()));
                    return loginRepository.login(loginDetails);
                });
    }
}
