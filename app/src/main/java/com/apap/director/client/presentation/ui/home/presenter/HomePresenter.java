package com.apap.director.client.presentation.ui.home.presenter;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.home.contract.HomeContract;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Adam on 2017-07-03.
 */

public class HomePresenter implements BasePresenter, HomeContract.Presenter {

    @Inject
    Realm realm;

    private HomeContract.View view;

    @Inject
    HomePresenter(HomeContract.View view) { this.view = view; }

    @Override
    public void dispose() {

    }

    @Override
    public void logOut(AccountEntity account) {
        realm.beginTransaction();
        account.setActive(false);
        realm.copyToRealmOrUpdate(account);
        realm.commitTransaction();
    }
}
