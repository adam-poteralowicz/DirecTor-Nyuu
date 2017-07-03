package com.apap.director.client.presentation.ui.login.presenter;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.interactor.base.Callback;
import com.apap.director.client.domain.interactor.login.GetAccountListInteractor;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.subscriptions.CompositeSubscription;


public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private GetAccountListInteractor getAccountListInteractor;

    @Inject
    public LoginPresenter(LoginContract.View view, GetAccountListInteractor getAccountListInteractor) {
        this.view = view;
        this.getAccountListInteractor = getAccountListInteractor;
    }

    @Override
    public void dispose() {
        getAccountListInteractor.dispose();
    }

    @Override
    public void getAccountList() {
        getAccountListInteractor.execute(null, new Callback<List<AccountEntity>>() {
            @Override
            public void onAccept(List<AccountEntity> data) {
                view.refreshAccountList(data);
            }

            @Override
            public void onError(Throwable throwable) {
                view.handleException(throwable);
            }
        });
    }
}
