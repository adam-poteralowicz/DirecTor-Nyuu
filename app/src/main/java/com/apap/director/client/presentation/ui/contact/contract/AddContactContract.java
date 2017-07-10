package com.apap.director.client.presentation.ui.contact.contract;

import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.support.v7.app.ActionBar;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

/**
 * Created by Adam Poterałowicz
 */

public interface AddContactContract {

    interface View extends BaseView {

        void showSnackbar(String text);

        void showActionBar(ActionBar actionBar);

        void showNewContact(String publicKey);

        void callPendingActivity();

        void getReadIntentFilters(IntentFilter ndefDetected);
    }

    interface Presenter extends BasePresenter {
        void readMessage(List<String> messages, ActionBar actionBar);

        void initNFC(NfcAdapter nfcAdapter);

        void useNFC(NfcAdapter nfcAdapter, IntentFilter ndefDetected);

        AccountEntity getActiveAccount();
    }
}
