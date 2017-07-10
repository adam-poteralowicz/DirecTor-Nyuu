package com.apap.director.client.presentation.ui.contact.contract;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

/**
 * Created by Adam Poterałowicz
 */

public interface NewContactContract {

    interface View extends BaseView {

        void showSnackbar(String text);

        void handleSuccess(String text);
    }

    interface Presenter extends BasePresenter {

        void addContact(String name, String publicKey);
    }
}
