package com.apap.director.client.presentation.ui.register.presenter;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.interactor.base.Callback;
import com.apap.director.client.domain.interactor.register.CreateAccountInteractor;
import com.apap.director.client.domain.interactor.register.RegisterAccountInteractor;
import com.apap.director.client.presentation.ui.register.contract.RegisterContract;

import javax.inject.Inject;

import okhttp3.ResponseBody;

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;
    private CreateAccountInteractor createAccountInteractor;
    private RegisterAccountInteractor registerAccountInteractor;

    @Inject
    public RegisterPresenter(RegisterContract.View view, CreateAccountInteractor createAccountInteractor, RegisterAccountInteractor registerAccountInteractor) {
        this.view = view;
        this.createAccountInteractor = createAccountInteractor;
        this.registerAccountInteractor = registerAccountInteractor;
    }

    @Override
    public void dispose() {
        createAccountInteractor.dispose();
        registerAccountInteractor.dispose();
    }

    @Override
    public void signUp(String name) {
        Log.v(RegisterPresenter.class.getSimpleName(), "Signing up as " + name);
        createAccountInteractor.execute(name, new Callback<AccountEntity>() {
            @Override
            public void onAccept(AccountEntity data) {
                Log.v(RegisterPresenter.class.getSimpleName(), "Account created");
                registerAccount(data);
            }

            @Override
            public void onError(Throwable throwable) {
                view.handleException(throwable);
            }
        });
    }

    private void registerAccount(AccountEntity account) {
        Log.v(RegisterPresenter.class.getSimpleName(), "Sign up request");

        registerAccountInteractor.execute(account.getKeyBase64(), new Callback<ResponseBody>() {

            @Override
            public void onAccept(ResponseBody data) {
                view.handleSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                view.handleException(throwable);
            }
        });
    }
}
