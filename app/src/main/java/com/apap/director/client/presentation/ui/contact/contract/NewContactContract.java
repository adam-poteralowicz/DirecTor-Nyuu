package com.apap.director.client.presentation.ui.contact.contract;

import android.content.Context;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

/**
 * Created by Adam on 2017-07-03.
 */

public interface NewContactContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void addContact(String name, String publicKey, Context context);
        void addConversation(String contactName);
    }
}
