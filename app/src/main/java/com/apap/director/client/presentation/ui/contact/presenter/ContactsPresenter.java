package com.apap.director.client.presentation.ui.contact.presenter;

import com.apap.director.client.data.db.mapper.AccountMapper;
import com.apap.director.client.domain.interactor.account.GetActiveAccountInteractor;
import com.apap.director.client.domain.interactor.contact.GetContactListInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.ContactsContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Adam Poterałowicz
 */

public class ContactsPresenter implements BasePresenter, ContactsContract.Presenter {

    private ContactsContract.View view;
    private GetActiveAccountInteractor getActiveAccountInteractor;
    private GetContactListInteractor getContactListInteractor;

    private CompositeDisposable subscriptions;
    private AccountMapper accountMapper;

    @Inject
    public ContactsPresenter(ContactsContract.View view, GetActiveAccountInteractor getActiveAccountInteractor, GetContactListInteractor getContactListInteractor) {
        this.view = view;
        this.getActiveAccountInteractor = getActiveAccountInteractor;
        this.getContactListInteractor = getContactListInteractor;
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void getActiveAccount() {
        subscriptions.add(getActiveAccountInteractor.execute(null)
                .subscribe(accountModel -> view.retrieveActiveAccount(accountMapper.mapToEntity(accountModel)),
                        throwable -> view.handleException(throwable)));
    }

    @Override
    public void getContactList() {
        subscriptions.add(getContactListInteractor.execute(null)
                .subscribe(contactEntities -> view.refreshContactList(contactEntities),
                        throwable -> view.handleException(throwable)));
    }
}
