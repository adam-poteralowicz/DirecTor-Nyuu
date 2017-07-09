package com.apap.director.client.presentation.ui.contact.presenter;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.domain.interactor.base.Callback;
import com.apap.director.client.domain.interactor.account.GetActiveAccountInteractor;
import com.apap.director.client.domain.interactor.contact.GetContactListInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.ContactsContract;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Adam Poterałowicz
 */

public class ContactsPresenter implements BasePresenter, ContactsContract.Presenter {

    private ContactsContract.View view;
    private GetActiveAccountInteractor getActiveAccountInteractor;
    private GetContactListInteractor getContactListInteractor;

    @Inject
    public ContactsPresenter(ContactsContract.View view, GetActiveAccountInteractor getActiveAccountInteractor, GetContactListInteractor getContactListInteractor) {
        this.view = view;
        this.getActiveAccountInteractor = getActiveAccountInteractor;
        this.getContactListInteractor = getContactListInteractor;
    }

    @Override
    public void dispose() {
        getActiveAccountInteractor.dispose();
        getContactListInteractor.dispose();
    }

    @Override
    public void getActiveAccount() {
        getActiveAccountInteractor.execute(null, new Callback<AccountEntity>() {

            @Override
            public void onAccept(AccountEntity account) {
                view.retrieveActiveAccount(account);
            }

            @Override
            public void onError(Throwable throwable) {
                view.handleException(throwable);
            }
        });
    }

    @Override
    public void getContactList() {
        getContactListInteractor.execute(null, new Callback<List<ContactEntity>>() {

            @Override
            public void onAccept(List<ContactEntity> data) {
                view.refreshContactList(data);
            }

            @Override
            public void onError(Throwable throwable) {
                view.handleException(throwable);
            }
        });
    }
}
