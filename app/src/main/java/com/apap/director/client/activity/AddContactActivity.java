package com.apap.director.client.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
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
import com.apap.director.client.util.NFCUtils;
import com.apap.director.db.realm.model.Account;
import com.apap.director.manager.AccountManager;
import com.apap.director.manager.ContactManager;
import com.apap.director.manager.ConversationManager;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;

import java.security.SecureRandom;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AddContactActivity extends AppCompatActivity {

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

    public static final String TAG = "DirecTor";
    public NfcAdapter nfcAdapter;
    public Intent intent;
    public PendingIntent pendingIntent;
    public IntentFilter[] readIntentFilters, writeIntentFilters;
    private final String MIME_TYPE = "text/plain";
    private byte[] myPublicKey;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        try {

            Account account = accountManager.getActiveAccount();
            IdentityKeyPair keyPair = null;
            keyPair = new IdentityKeyPair(account.getKeyPair());
            byte[] key = keyPair.getPublicKey().serialize();
            myPublicKey = Base64.encode(key, Base64.NO_WRAP | Base64.URL_SAFE);

            myKeyView.setText("My public key: " + new String(myPublicKey));

        } catch (InvalidKeyException e) {
            Log.getStackTraceString(e);
        }

        getSupportActionBar().show();
        initNFC();
    }

    @Override
    // on public key response received
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            readMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            enableNdefExchangeMode();
        } catch (InvalidKeyException e) {
            Log.getStackTraceString(e);
        }
        enableTagWriteMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.atn_nfc) {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_NFC) && NfcAdapter.getDefaultAdapter(this) != null) {
                if (nfcAdapter.isEnabled()) {
                    Toast.makeText(this, R.string.nfc_enabled, Toast.LENGTH_LONG).show();
                    useNFC();
                } else {
                    Toast.makeText(this, R.string.activate_nfc, Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        Intent selectedIntent = new Intent(AddContactActivity.this, AuthUserActivity.class);
        startActivity(selectedIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void initNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter.isEnabled()) {
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }
    }

    public void useNFC() {
        if (nfcAdapter == null) {
            Toast.makeText(this, "This device does not support NFC", Toast.LENGTH_LONG).show();
            return;
        }

        if (nfcAdapter.isEnabled()) {
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try {
                ndefDetected.addDataType(MIME_TYPE);
            } catch (MalformedMimeTypeException e) {
                Log.getStackTraceString(e);
            }

            readIntentFilters = new IntentFilter[]{ndefDetected};
        }
    }

    // Exchange public key with another user
    private void enableNdefExchangeMode() throws InvalidKeyException {

        NdefMessage message = NFCUtils.getNewMessage(MIME_TYPE, myPublicKey);

        if (nfcAdapter != null) {
            // Automatically beams the message when two devices are in close enough proximity.
            nfcAdapter.setNdefPushMessage(message, this);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, readIntentFilters, null);
        }
    }

    private void enableTagWriteMode() {
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        writeIntentFilters = new IntentFilter[]{tagDetected};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeIntentFilters, null);
    }

    public void readMessage() {
        List<String> msgs = NFCUtils.getStringsFromNfcIntent(intent);
        if (msgs != null) {
            Toast.makeText(this, "Contact public key : " + msgs.get(0), Toast.LENGTH_LONG).show();
            String contactPublicKey = msgs.get(0);
            getSupportActionBar().show();

            Intent intent = new Intent(this, NewContactActivity.class);
            intent.putExtra("key", contactPublicKey);
            startActivity(intent);
        }
    }

    private void setUpNewIntent(String key) {
        Intent newContactIntent = new Intent(this, NewContactActivity.class);
        newContactIntent.putExtra("key", key);
        startActivity(newContactIntent);
    }

}
