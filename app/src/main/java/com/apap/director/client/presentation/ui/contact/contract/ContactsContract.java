package com.apap.director.client.presentation.ui.contact.contract;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

/**
 * Created by Adam on 2017-07-04.
 */

public interface ContactsContract {

    interface View extends BaseView {

        void retrieveActiveAccount(AccountEntity account);

        void refreshContactList(List<ContactEntity> data);
    }

    interface Presenter extends BasePresenter {

        void getActiveAccount();

        void getContactList();
    }
}
