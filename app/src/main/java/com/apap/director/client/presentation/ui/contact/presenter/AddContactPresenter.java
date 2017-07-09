package com.apap.director.client.presentation.ui.contact.presenter;

import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.AddContactContract;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Adam Poterałowicz
 */

public class AddContactPresenter implements BasePresenter, AddContactContract.Presenter {

    private static final String MIME_TYPE = "text/plain";
    private AddContactContract.View view;

    @Inject
    AddContactPresenter(AddContactContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {

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
}

