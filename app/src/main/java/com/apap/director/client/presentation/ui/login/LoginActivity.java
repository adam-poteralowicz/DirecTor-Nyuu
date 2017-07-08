package com.apap.director.client.presentation.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.data.net.service.ClientService;
import com.apap.director.client.presentation.ui.common.view.NetActivity;
import com.apap.director.client.presentation.ui.home.HomeActivity;
import com.apap.director.client.presentation.ui.login.adapter.AccountAdapter;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;
import com.apap.director.client.presentation.ui.login.di.component.DaggerLoginContractComponent;
import com.apap.director.client.presentation.ui.login.di.module.LoginContractModule;
import com.apap.director.client.presentation.ui.login.presenter.LoginPresenter;
import com.apap.director.client.presentation.ui.password.PasswordActivity;
import com.apap.director.client.presentation.ui.register.NewAccountActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import info.guardianproject.netcipher.NetCipher;
import io.realm.Realm;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends NetActivity implements LoginContract.View {

    private static final String TAG = App.getContext().getClass().getSimpleName();
    private static final int LAYOUT_ID = R.layout.login_view;

    @Inject
    AccountManager accountManager;
    @Inject
    RestAccountService restAccountService;
    @Inject
    Realm realm;
    @Inject
    LoginPresenter loginPresenter;

    @BindView(R.id.accountsView)
    RecyclerView accountsRecyclerView;
    @BindView(R.id.loginActivity_dialog)
    View masterPasswordDialog;
    @BindView(R.id.masterPasswordVerification_button)
    Button verificationButton;
    @BindView(R.id.masterPasswordVerification_editText)
    EditText masterPasswordEditText;
    @BindView(R.id.loginActivity_layout)
    View rootLayout;

    private ArrayList<AccountEntity> accountList;
    private ArrayAdapter<AccountEntity> arrayAdapter;
    private String accountName;
    private SharedPreferences.Editor prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);

        ButterKnife.bind(this);

        setUpInjection();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        AccountAdapter accountAdapter = new AccountAdapter(this);
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountsRecyclerView.setAdapter(accountAdapter);
    }

    private void setUpInjection() {
        DaggerLoginContractComponent.builder()
                .mainComponent(((App) getApplication()).getComponent())
                .loginContractModule(new LoginContractModule(this))
                .build()
                .inject(this);
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
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.postLoginButton)
    public void onClick(View view) {
        try {
            if (accountManager.getActiveAccount() == null) {
                Snackbar.make(rootLayout, "Choose an account", Snackbar.LENGTH_LONG).show();
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
            if (getSharedPreferences("prefs", MODE_PRIVATE).contains("masterPassword")) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
            }

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
            Snackbar.make(rootLayout, "Wrong master password", Snackbar.LENGTH_LONG).show();
        }
    }

    @OnItemClick(R.id.accountsView)
    public void chooseAccount(int position) {
        boolean success = accountManager.chooseAccount(accountList.get(position).getName());
        Snackbar.make(rootLayout, accountManager.getActiveAccountName() + " " + success, Snackbar.LENGTH_LONG).show();
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

    @Override
    public void refreshAccountList(List<AccountEntity> newList) {
        arrayAdapter.clear();
        arrayAdapter.addAll(newList);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
    }
}