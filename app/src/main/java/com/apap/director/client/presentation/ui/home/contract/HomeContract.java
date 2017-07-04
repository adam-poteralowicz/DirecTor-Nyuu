package com.apap.director.client.presentation.ui.home.contract;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

/**
 * Created by Adam on 2017-07-03.
 */

public interface HomeContract {

    interface View extends BaseView {
        void logOut(AccountEntity account);
    }

    interface Presenter extends BasePresenter {
        void logOut(AccountEntity account);
    }
}
