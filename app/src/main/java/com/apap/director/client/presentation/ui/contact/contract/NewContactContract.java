package com.apap.director.client.presentation.ui.contact.contract;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

/**
 * Created by Adam on 2017-07-03.
 */

public interface NewContactContract {

    interface View extends BaseView {
        void showToast(String text);
        void addConversation(String contactName);
    }

    interface Presenter extends BasePresenter {
        void addContact(String name, String publicKey);
        void addConversation(String contactName);
    }
}
