package com.apap.director.client.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import android.widget.EditText;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.util.NFCUtils;
import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.manager.AccountManager;
import com.apap.director.manager.ContactManager;
import com.apap.director.manager.ConversationManager;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class AddContactActivity extends AppCompatActivity {

    @BindView(R.id.newContactName) EditText newContactName;
    public static final String TAG = "DirecTor";
    public NfcAdapter _nfcAdapter;
    public Intent _intent;
    public PendingIntent _pendingIntent;
    public IntentFilter[] _readIntentFilters, _writeIntentFilters;
    private final String _MIME_TYPE = "text/plain";
    private String contactPublicKey;
    private byte[] myPublicKey;

    @Inject Realm realm;
    @Inject ContactManager contactManager;
    @Inject AccountManager accountManager;
    @Inject ConversationManager conversationManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        newContactName = (EditText) findViewById(R.id.newContactName);
        newContactName.setHint("CONTACT NAME");
        ((App) getApplication()).getComponent().inject(this);
        getSupportActionBar().show();
        initNFC();
    }

    @OnClick(R.id.addContactButton)
    public void onClick() {
        String name = String.valueOf(newContactName.getText());
        if (name.matches(".*\\w.*")
                && (name.matches("\\w.*")))
        {
            contactManager.addContact(name, contactPublicKey);

            Realm realm = Realm.getDefaultInstance();
                Contact contact = realm.where(Contact.class).equalTo("name", name).findFirst();
                conversationManager.addConversation(contact, null);
            realm.close();

            Toast.makeText(this, contactPublicKey, Toast.LENGTH_LONG).show();

            Intent selectedIntent = new Intent(AddContactActivity.this, AuthUserActivity.class);
            startActivityForResult(selectedIntent, 13);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void initNFC() {
        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (_nfcAdapter.isEnabled()) {
            _pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }
    }

    public void useNFC() {
        if (_nfcAdapter == null) {
            Toast.makeText(this, "This device does not support NFC", Toast.LENGTH_LONG).show();
            return;
        }

        if (_nfcAdapter.isEnabled()) {
            _pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try {
                ndefDetected.addDataType(_MIME_TYPE);
            } catch (MalformedMimeTypeException e) {
                Log.e(this.toString(), e.getMessage());
            }

            _readIntentFilters = new IntentFilter[] { ndefDetected };
        }
    }

    // Exchange public key with another user
    private void _enableNdefExchangeMode() throws InvalidKeyException {
        Account account = accountManager.getActiveAccount();
        IdentityKeyPair keyPair = new IdentityKeyPair(account.getKeyPair());
        byte[] key = keyPair.getPublicKey().serialize();
        myPublicKey = Base64.encode(key, Base64.NO_WRAP | Base64.URL_SAFE);
        Log.d("MY PUBLIC KEY", new String(myPublicKey));
        Log.d("MY PUBLIC KEY2", Base64.encodeToString(key, Base64.NO_WRAP | Base64.URL_SAFE));

        NdefMessage message = NFCUtils.getNewMessage(_MIME_TYPE, myPublicKey);

        if (_nfcAdapter != null) {
            // Automatically beams the message when two devices are in close enough proximity.
            _nfcAdapter.setNdefPushMessage(message, this);
            _nfcAdapter.enableForegroundDispatch(this, _pendingIntent, _readIntentFilters, null);
        }
    }

    private void _enableTagWriteMode() {
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        _writeIntentFilters = new IntentFilter[] { tagDetected };
        _nfcAdapter.enableForegroundDispatch(this, _pendingIntent, _writeIntentFilters, null);
    }

    @Override
    // on public key response received
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        _intent = intent;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            _readMessage();
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            _readMessage();
        }
    }

//    public NdefMessage _getNdefMessage() {
//        return NFCUtils.getNewMessage(_MIME_TYPE, publicKey);
//    }

    public void _readMessage() {
        List<String> msgs = NFCUtils.getStringsFromNfcIntent(_intent);
        if (msgs != null) {
            Toast.makeText(this, "Contact public key : " + msgs.get(0), Toast.LENGTH_LONG).show();
            contactPublicKey = msgs.get(0);
            getSupportActionBar().show();
        }
    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity
     * to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();

        try {
            _enableNdefExchangeMode();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        _enableTagWriteMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.atn_nfc:
                PackageManager pm = getPackageManager();
                if(pm.hasSystemFeature(PackageManager.FEATURE_NFC) && NfcAdapter.getDefaultAdapter(this) != null) {
                    if (_nfcAdapter.isEnabled()) {
                        Toast.makeText(this, "NFC enabled", Toast.LENGTH_LONG).show();
                        useNFC();
                    } else {
                        Toast.makeText(this, "Please activate NFC", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "NFC feature not supported", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent selectedIntent = new Intent(AddContactActivity.this, AuthUserActivity.class);
        startActivity(selectedIntent);
    }

}
