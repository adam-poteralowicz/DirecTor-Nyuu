package com.apap.director.client.presentation.ui.home.contract;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

/**
 * Created by Adam Poterałowicz
 */

public interface HomeContract {

    interface View extends BaseView {

        void handleSuccess(String message);
    }

    interface Presenter extends BasePresenter {

        void logOut(AccountEntity account);

        AccountEntity getActiveAccount();
    }
}
