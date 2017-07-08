package com.apap.director.client.presentation.ui.contact;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.data.util.NFCUtils;
import com.apap.director.client.presentation.ui.contact.contract.AddContactContract;
import com.apap.director.client.presentation.ui.contact.di.component.DaggerAddContactComponent;
import com.apap.director.client.presentation.ui.contact.di.module.AddContactContractModule;
import com.apap.director.client.presentation.ui.contact.presenter.AddContactPresenter;
import com.apap.director.client.presentation.ui.home.HomeActivity;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;

import java.security.SecureRandom;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AddContactActivity extends AppCompatActivity implements AddContactContract.View {

    private static final String MIME_TYPE = "text/plain";

    @Inject
    AddContactPresenter addContactPresenter;
    @Inject
    Realm realm;
    @Inject
    ContactManager contactManager;
    @Inject
    AccountManager accountManager;
    @Inject
    ConversationManager conversationManager;

    @BindView(R.id.myPublicKey)
    TextView myKeyView;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] readIntentFilters;
    private byte[] myPublicKey;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpInjection();
        ButterKnife.bind(this);

        try {
            IdentityKeyPair keyPair = new IdentityKeyPair(accountManager.getActiveAccount().getKeyPair());
            myPublicKey = Base64.encode(keyPair.getPublicKey().serialize(), Base64.NO_WRAP | Base64.URL_SAFE);
            myKeyView.setText("My public key: " + new String(myPublicKey));
        } catch (InvalidKeyException e) {
            Log.getStackTraceString(e);
        }
        getSupportActionBar().show();

        addContactPresenter.initNFC(NfcAdapter.getDefaultAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.atn_nfc) {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_NFC) && NfcAdapter.getDefaultAdapter(this) != null) {
                if (nfcAdapter.isEnabled()) {
                    Toast.makeText(this, R.string.nfc_enabled, Toast.LENGTH_LONG).show();
                    addContactPresenter.useNFC(NfcAdapter.getDefaultAdapter(this), new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED));
                } else {
                    Toast.makeText(this, R.string.please_activate_nfc, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.nfc_not_supported, Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (item.getItemId() == R.id.atn_add_contact) {
            SecureRandom sr = new SecureRandom();
            setUpNewIntent("publicKey" + sr.nextInt() + sr.nextInt());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(newIntent.getAction()) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(newIntent.getAction())) {
            addContactPresenter.readMessage(NFCUtils.getStringsFromNfcIntent(newIntent), getSupportActionBar());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            exchangePublicKeys();
        } catch (InvalidKeyException e) {
            Log.getStackTraceString(e);
        }
        enableTagWriteMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        Intent selectedIntent = new Intent(AddContactActivity.this, HomeActivity.class);
        startActivity(selectedIntent);
    }

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showActionBar(ActionBar actionBar) {
        actionBar.show();
    }

    @Override
    public void showNewContact(String publicKey) {
        setUpNewIntent(publicKey);
    }

    @Override
    public void callPendingActivity() {
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    public void getReadIntentFilters(IntentFilter ndefDetected) {
        readIntentFilters = new IntentFilter[]{ndefDetected};
    }

    private void exchangePublicKeys() throws InvalidKeyException {
        NdefMessage message = NFCUtils.getNewMessage(MIME_TYPE, myPublicKey);

        if (nfcAdapter != null) {
            nfcAdapter.setNdefPushMessage(message, this);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, readIntentFilters, null);
        }
    }

    private void enableTagWriteMode() {
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] writeIntentFilters = new IntentFilter[]{tagDetected};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeIntentFilters, null);
    }

    private void setUpInjection() {
        DaggerAddContactComponent.builder()
                .mainComponent(((App) getApplication()).getComponent())
                .addContactContractModule(new AddContactContractModule(this))
                .build()
                .inject(this);
    }

    private void setUpNewIntent(String key) {
        Intent newContactIntent = new Intent(this, NewContactActivity.class);
        newContactIntent.putExtra("key", key);
        startActivity(newContactIntent);
    }
}
