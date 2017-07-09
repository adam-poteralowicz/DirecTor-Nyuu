package com.apap.director.client.presentation.ui.login.contract;

import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

/**
 * Created by Alicja Michniewicz
 */

public interface LoginContract {

    interface View extends BaseView {
        void refreshAccountList(List<AccountModel> newList);
    }

    interface Presenter extends BasePresenter {
        void getAccountList();
    }
}
