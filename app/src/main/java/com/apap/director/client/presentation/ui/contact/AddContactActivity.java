package com.apap.director.client.presentation.ui.contact;

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
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.presentation.ui.home.HomeActivity;
import com.apap.director.client.data.util.NFCUtils;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AddContactActivity extends AppCompatActivity {

    private static final String MIME_TYPE = "text/plain";

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
    private Intent intent;
    private PendingIntent pendingIntent;
    private IntentFilter[] readIntentFilters;
    private byte[] myPublicKey;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        try {
            IdentityKeyPair keyPair = new IdentityKeyPair(accountManager.getActiveAccount().getKeyPair());
            myPublicKey = Base64.encode(keyPair.getPublicKey().serialize(), Base64.NO_WRAP | Base64.URL_SAFE);
            myKeyView.setText("My public key: " + new String(myPublicKey));
        } catch (InvalidKeyException e) {
            Log.getStackTraceString(e);
        }

        getSupportActionBar().show();
        initNFC();
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
                    Toast.makeText(this, "NFC enabled", Toast.LENGTH_LONG).show();
                    useNFC();
                } else {
                    Toast.makeText(this, "Please activate NFC", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "NFC feature not supported", Toast.LENGTH_LONG).show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        intent = newIntent;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(newIntent.getAction())) {
            _readMessage();
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(newIntent.getAction())) {
            _readMessage();
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

    public void _readMessage() {
        List<String> msgs = NFCUtils.getStringsFromNfcIntent(intent);
        if (msgs != null) {
            Toast.makeText(this, "Contact public key : " + msgs.get(0), Toast.LENGTH_LONG).show();
            String contactPublicKey = msgs.get(0);
            getSupportActionBar().show();

            Intent newContactIntent = new Intent(this, NewContactActivity.class);
            newContactIntent.putExtra("key", contactPublicKey);
            startActivity(newContactIntent);
        }
    }
}
