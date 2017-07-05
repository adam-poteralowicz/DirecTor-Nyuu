package com.apap.director.client.presentation.ui.register.presenter;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.interactor.base.Callback;
import com.apap.director.client.domain.interactor.register.CreateAccountInteractor;
import com.apap.director.client.domain.interactor.register.RegisterAccountInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.presentation.ui.register.contract.RegisterContract;

import org.whispersystems.libsignal.util.KeyHelper;

import javax.inject.Inject;

import okhttp3.ResponseBody;

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;
    private RegisterAccountInteractor registerAccountInteractor;

    @Inject
    public RegisterPresenter(RegisterContract.View view, RegisterAccountInteractor registerAccountInteractor) {
        this.view = view;
        this.registerAccountInteractor = registerAccountInteractor;
    }

    @Override
    public void dispose() {
        registerAccountInteractor.dispose();
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

    @Override
    public void signUp(String name) {

    }

    private void createAccount(String name) {
        Log.v(RegisterPresenter.class.getSimpleName(), "Creating account named " + name);

        AccountModel accountModel = new AccountModel();
        accountModel.setName(name);
        accountModel.preconfigureAccount();
    }

    private void createSignedKey(AccountModel account) {
        KeyHelper.generateSignedPreKey(account.getKeyPair(), 
    }
}
