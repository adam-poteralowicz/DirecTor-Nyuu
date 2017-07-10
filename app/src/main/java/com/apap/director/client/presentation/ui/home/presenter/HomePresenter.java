package com.apap.director.client.presentation.ui.home.presenter;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.mapper.AccountMapper;
import com.apap.director.client.domain.interactor.account.GetActiveAccountInteractor;
import com.apap.director.client.domain.interactor.home.LogoutInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.home.contract.HomeContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Adam Poterałowicz
 */

public class HomePresenter implements BasePresenter, HomeContract.Presenter {

    private HomeContract.View view;
    private GetActiveAccountInteractor getActiveAccountInteractor;
    private LogoutInteractor logoutInteractor;

    private CompositeDisposable subscriptions;
    private AccountMapper accountMapper;

    @Inject
    HomePresenter(HomeContract.View view, GetActiveAccountInteractor getActiveAccountInteractor, LogoutInteractor logoutInteractor) {
        this.view = view;
        this.getActiveAccountInteractor = getActiveAccountInteractor;
        this.logoutInteractor = logoutInteractor;

        subscriptions = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void logOut(AccountEntity account) {
        subscriptions.add(logoutInteractor.execute(accountMapper.mapToModel(account))
                .subscribe(accountModel -> view.handleSuccess(accountModel.getName() + " logged out"),
                        throwable -> view.handleException(throwable)));
    }

    @Override
    public AccountEntity getActiveAccount() {
        final AccountEntity[] entity = {new AccountEntity()};
        subscriptions.add(getActiveAccountInteractor.execute(null)
                .subscribe(accountModel -> {
                            entity[0] = accountMapper.mapToEntity(accountModel);
                        },
                        throwable -> view.handleException(throwable)));

        return entity[0];
    }
}
