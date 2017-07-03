package com.apap.director.client.presentation.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.net.rest.service.UserService;
import com.apap.director.client.data.net.service.ClientService;
import com.apap.director.client.presentation.ui.common.view.NetActivity;
import com.apap.director.client.presentation.ui.home.HomeActivity;
import com.apap.director.client.presentation.ui.listener.ArrayAdapterChangeListener;
import com.apap.director.client.presentation.ui.register.NewAccountActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import info.guardianproject.netcipher.NetCipher;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends NetActivity {

    private static final String HS_URL = "http://3zk5ak4bcbfvwgha.onion";
    private String TAG = App.getContext().getClass().getSimpleName();

    @Inject
    AccountManager accountManager;
    @Inject
    UserService userService;
    @Inject
    Realm realm;

    @BindView(R.id.accountsView)
    ListView accountsListView;
    @BindView(R.id.loginActivity_dialog)
    View masterPasswordDialog;
    @BindView(R.id.masterPasswordVerification_button)
    Button verificationButton;
    @BindView(R.id.masterPasswordVerification_editText)
    EditText masterPasswordEditText;

    private ArrayList<AccountEntity> accountList;
    private RealmResults<AccountEntity> realmAccounts;
    private ArrayAdapterChangeListener<AccountEntity, RealmResults<AccountEntity>> listener;
    private String accountName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        realmAccounts = realm.where(AccountEntity.class).findAll();

        accountList = new ArrayList<>();
        ArrayAdapter<AccountEntity> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_single_choice,
                accountList);
        accountsListView.setAdapter(arrayAdapter);

        arrayAdapter.addAll(realmAccounts);
        arrayAdapter.notifyDataSetChanged();

        listener = new ArrayAdapterChangeListener<>(arrayAdapter, "login activity");
        realmAccounts.addChangeListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tor_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tor_conn_on:
                NetCipher.useTor();
                return true;
            case R.id.tor_conn_off:
                NetCipher.clearProxy();
                return true;
            case R.id.swapId:
                Log.v(TAG, "Swap Id pressed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            accountManager.signUp(accountManager.createAccount(data.getStringExtra("accountName")));
        }
    }

    @Override
    protected void onStart() {
        realmAccounts.addChangeListener(listener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        realmAccounts.removeChangeListener(listener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmAccounts.removeChangeListener(listener);
    }

    @Override
    public void onBackPressed() {
        ClientService.disconnect();
    }

    @OnClick(R.id.postLoginButton)
    public void onClick(View view) {
        try {
            if (accountManager.getActiveAccount() == null) {
                Toast.makeText(this, "Choose an account", Toast.LENGTH_LONG).show();
                return;
            } else Log.d("active account", accountManager.getActiveAccountName());

            String cookie = null;

            for (int i = 0; i < 3; i++) {
                AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return accountManager.logIn();
                    }
                };
                cookie = asyncTask.execute().get();
                if (cookie != null)
                    break;
            }


            AsyncTask<Void, Void, Void> keysTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Log.v(TAG, "Trying to post keys");
                        accountManager.postSignedKey();
                        accountManager.postOneTimeKeys();

                    } catch (IOException e) {
                        Log.getStackTraceString(e);
                    }
                    return null;
                }
            };

            keysTask.execute().get();

            Log.v(TAG, "Chosen account: " + accountManager.getActiveAccount() + " cookie" + accountManager.getActiveAccount().getCookie());

            realm.beginTransaction();
            AccountEntity account = realm.where(AccountEntity.class).equalTo("active", true).findFirst();
            String cookie2 = account.getCookie();
            Log.v(TAG, "cookie2 " + cookie2);
            realm.commitTransaction();

            ClientService.connect(cookie);
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

        } catch (InterruptedException | ExecutionException e) {
            Log.getStackTraceString(e);
        }
    }

    @OnClick(R.id.newAccButton)
    public void newAccount(View view) {
        Intent newAccIntent = new Intent(LoginActivity.this, NewAccountActivity.class);
        startActivityForResult(newAccIntent, 1);
    }

    @OnClick(R.id.masterPasswordVerification_button)
    public void verifyInput(View view) {
        if (verifyMasterPassword(String.valueOf(masterPasswordEditText.getText()))) {
            deleteAccount(accountName);
            masterPasswordDialog.setVisibility(GONE);
        } else {
            Toast.makeText(this, "Wrong master password", Toast.LENGTH_LONG).show();
        }
    }

    @OnItemClick(R.id.accountsView)
    public void chooseAccount(int position) {
        boolean success = accountManager.chooseAccount(accountList.get(position).getName());
        Toast.makeText(this, accountManager.getActiveAccountName() + " " + success, Toast.LENGTH_LONG).show();
    }

    @OnItemLongClick(R.id.accountsView)
    public boolean displayVerificationDialog(int position) {
        accountName = accountList.get(position).getName();
        masterPasswordDialog.setVisibility(VISIBLE);
        return true;
    }

    public boolean verifyMasterPassword(String password) {
        return realm.where(AccountEntity.class).equalTo("masterPassword", password) != null;
    }

    public void deleteAccount(String accountName) {
        boolean deleted = accountManager.deleteAccount(accountName);
        if (!deleted)
            Log.v(TAG, accountName + " account failed to delete");
        else
            Log.v(TAG, accountName + " account deleted");
    }
}