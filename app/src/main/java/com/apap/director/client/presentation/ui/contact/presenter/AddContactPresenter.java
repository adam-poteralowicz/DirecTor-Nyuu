package com.apap.director.client.presentation.ui.contact.presenter;

import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.mapper.AccountMapper;
import com.apap.director.client.domain.interactor.account.GetActiveAccountInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.AddContactContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Adam Poterałowicz
 */

public class AddContactPresenter implements BasePresenter, AddContactContract.Presenter {

    private static final String MIME_TYPE = "text/plain";
    private AddContactContract.View view;
    private GetActiveAccountInteractor getActiveAccountInteractor;

    private CompositeDisposable subscriptions;
    private AccountMapper accountMapper;

    @Inject
    AddContactPresenter(AddContactContract.View view, GetActiveAccountInteractor getActiveAccountInteractor) {
        this.view = view;
        this.getActiveAccountInteractor = getActiveAccountInteractor;

        subscriptions = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void readMessage(List<String> messages, ActionBar actionBar) {
        if (messages != null) {
            view.showSnackbar("ContactEntity public key : " + messages.get(0));
            view.showActionBar(actionBar);
            view.showNewContact(messages.get(0));
        }
    }

    @Override
    public void initNFC(NfcAdapter nfcAdapter) {
        if (nfcAdapter.isEnabled()) {
            view.callPendingActivity();
        }
    }

    @Override
    public void useNFC(NfcAdapter nfcAdapter, IntentFilter ndefDetected) {
        if (nfcAdapter == null) {
            view.showSnackbar("This device does not support NFC");
        } else if (nfcAdapter.isEnabled()) {
            view.callPendingActivity();

            try {
                ndefDetected.addDataType(MIME_TYPE);
            } catch (IntentFilter.MalformedMimeTypeException e) {
                Log.getStackTraceString(e);
            }

            view.getReadIntentFilters(ndefDetected);
        }
    }

    @Override
    public AccountEntity getActiveAccount() {
        final AccountEntity[] entity = {new AccountEntity()};
        subscriptions.add(getActiveAccountInteractor.execute(null)
                .subscribe(accountModel -> entity[0] = accountMapper.mapToEntity(accountModel),
                        throwable -> view.handleException(throwable)));

        return entity[0];
    }
}

