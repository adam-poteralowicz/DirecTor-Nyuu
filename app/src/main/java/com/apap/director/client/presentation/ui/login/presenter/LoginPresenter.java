package com.apap.director.client.presentation.ui.login.presenter;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;

import javax.inject.Inject;


public class LoginPresenter implements BasePresenter {

    private LoginContract.View view;

    @Inject
    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {

    }
}
