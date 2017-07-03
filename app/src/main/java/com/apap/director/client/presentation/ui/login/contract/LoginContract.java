package com.apap.director.client.presentation.ui.login.contract;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

/**
 * Created by Alicja Michniewicz
 */

public interface LoginContract {

    interface View extends BaseView {
        void refreshAccountList(List<AccountEntity> newList);
    }

    interface Presenter extends BasePresenter {
        void getAccountList();
    }
}
