package com.apap.director.client.presentation.ui.login.presenter;

import com.apap.director.client.domain.interactor.login.GetAccountListInteractor;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private GetAccountListInteractor getAccountListInteractor;
    private CompositeDisposable subscriptions;

    @Inject
    public LoginPresenter(LoginContract.View view, GetAccountListInteractor getAccountListInteractor) {
        this.view = view;
        this.getAccountListInteractor = getAccountListInteractor;

        subscriptions = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void getAccountList() {
        subscriptions.add(getAccountListInteractor.execute(null)
                .subscribe(data -> view.refreshAccountList(data),
                        throwable -> view.handleException(throwable)));
    }
}
