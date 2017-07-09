package com.apap.director.client.presentation.ui.login.contract;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

/**
 * Created by Alicja Michniewicz
 */

public interface LoginContract {

    interface View extends BaseView {

        void refreshAccountList(List<AccountEntity> entities);

        void handleSuccess(String message);
    }

    interface Presenter extends BasePresenter {

        void getAccountList();

        AccountEntity getActiveAccount();

        void signUp(AccountEntity accountEntity);

        AccountEntity createAccount(String name);

        String logIn(AccountModel accountModel);

        void postOneTimeKeys(AccountModel accountModel);

        void postSignedKey(AccountModel accountModel);

        void chooseAccount(String name);

        void deleteAccount(String name);
    }
}
