package com.apap.director.client.presentation.ui.login;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.mapper.AccountMapper;
import com.apap.director.client.data.net.service.WebSocketService;
import com.apap.director.client.presentation.ui.common.view.NetActivity;
import com.apap.director.client.presentation.ui.home.HomeActivity;
import com.apap.director.client.presentation.ui.login.adapter.AccountAdapter;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;
import com.apap.director.client.presentation.ui.login.di.component.DaggerLoginContractComponent;
import com.apap.director.client.presentation.ui.login.di.module.LoginContractModule;
import com.apap.director.client.presentation.ui.login.presenter.LoginPresenter;
import com.apap.director.client.presentation.ui.register.NewAccountActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import io.realm.Realm;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends NetActivity implements LoginContract.View {

    private static final String TAG = App.getContext().getClass().getSimpleName();
    private static final int LAYOUT_ID = R.layout.login_view;
    private static final String SHARED_PREFERENCES_FILENAME = "prefs";
    private static final String KEY = "masterPassword";


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

    @BindView(R.id.postLoginButton)
    Button loginButton;

    private ArrayList<AccountEntity> accountList;
    private String accountName;
    private AccountAdapter accountAdapter;
    private AccountMapper accountMapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);

        ButterKnife.bind(this);

        setUpInjection();
        setUpRecyclerView();

        loginButton.setEnabled(false);
        loginButton.setAlpha(0.5f);
    }

    private void setUpRecyclerView() {
        accountAdapter = new AccountAdapter(this);
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
        inflater.inflate(R.menu.login_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.swapId) {
            Log.v(TAG, "Swap Id pressed");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            loginPresenter.signUp(loginPresenter.createAccount(data.getStringExtra("accountName")));
        }
    }

    @OnClick(R.id.postLoginButton)
    public void onClick(View view) {
        if (loginPresenter.getActiveAccount() == null) {
            Snackbar.make(rootLayout, "Choose an account", Snackbar.LENGTH_LONG).show();
            return;
        } else Log.d("active account", loginPresenter.getActiveAccount().getName());

        Log.v(TAG, "Chosen account: " + loginPresenter.getActiveAccount() + " cookie" + loginPresenter.getActiveAccount().getCookie());


        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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
        loginPresenter.chooseAccount(accountList.get(position).getName());
        loginButton.setEnabled(true);
        loginButton.setAlpha(1);
    }

    @OnItemLongClick(R.id.accountsView)
    public boolean displayVerificationDialog(int position) {
        accountName = accountList.get(position).getName();
        masterPasswordDialog.setVisibility(VISIBLE);
        return true;
    }

    public boolean verifyMasterPassword(String password) {
        return password.equals(getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE).getString(KEY, ""));
    }

    public void deleteAccount(String accountName) {
        loginPresenter.deleteAccount(accountName);
    }

    @Override
    public void refreshAccountList(List<AccountEntity> newList) {
        accountAdapter.clear();
        accountAdapter.update(newList);
    }

    @Override
    public void handleSuccess(String message) {
        Log.v(TAG, message);
    }

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
    }
}