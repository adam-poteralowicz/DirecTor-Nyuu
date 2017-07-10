package com.apap.director.client.presentation.ui.register.contract;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

/**
 * Created by Alicja Michniewicz
 */

public interface RegisterContract {

    interface Presenter extends BasePresenter {

        void signUp(String name);
    }

    interface View extends BaseView {

        void handleSuccess();
    }

}
