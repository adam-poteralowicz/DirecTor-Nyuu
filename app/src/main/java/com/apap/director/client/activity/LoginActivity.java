package com.apap.director.client.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.db.realm.AccountManager;
import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.rest.service.UserService;
import com.apap.director.im.websocket.service.StompService;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import info.guardianproject.netcipher.NetCipher;
import info.guardianproject.netcipher.client.StrongBuilder;
import info.guardianproject.netcipher.client.StrongHttpClientBuilder;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity implements StrongBuilder.Callback<HttpClient> {

    Shimmer shimmer;
//    String HS_URL = "http://3zk5ak4bcbfvwgha.onion";
    String HS_URL = "http://www.wp.pl/static.html";

    @Inject
    StompService service;

    @BindView(R.id.accountsView) ListView accountsListView;
    private Realm realm;
    private ArrayList<Account> accountList;
    private AccountManager accountManager;
    private UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        ((App) getApplication()).getSignalComponent().inject(this);
        ButterKnife.bind(this);

        try {
            StrongHttpClientBuilder builder = new StrongHttpClientBuilder(this);
            StrongHttpClientBuilder
                    .forMaxSecurity(this)
                    .withTorValidation()
                    .build(this);
            Log.d("Builder status", String.valueOf(builder.supportsHttpProxy())+"\n"+String.valueOf(builder.supportsSocksProxy())+"\n"+builder.toString());
        }
        catch (Exception e) {
            Toast.makeText(this, R.string.msg_crash, Toast.LENGTH_LONG)
                    .show();
            Log.e(getClass().getSimpleName(),
                    "Exception loading hidden service", e);
            finish();
        }

        ShimmerTextView shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_tv);
        shimmerTextView.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{}
                },
                new int[] {
                        Color.argb(255, 102, 102, 255)
                }
        ));
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
        getSupportActionBar().show();

        /**
         * Account List View Attempt
         */

        realm = Realm.getDefaultInstance();
        final RealmResults<Account> accounts = realm.where(Account.class).findAll();
        accounts.addChangeListener(new RealmChangeListener<RealmResults<Account>>() {
            @Override
            public void onChange(RealmResults<Account> results) {
                accounts.size();
            }
        });

        accountManager = new AccountManager(userService);
        accountList = new ArrayList<Account>();

        ArrayList<Account> realmAccounts = accountManager.listAllAccounts();

        if (!realmAccounts.isEmpty()) {
            accountList = realmAccounts;
        }

        ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<Account>(
                App.getContext(),
                android.R.layout.simple_list_item_single_choice,
                accountList);
        if (accountList.isEmpty())
            Log.d("account list", "empty");
        if (accountsListView == null)
            Log.d("accounts list view", "null");
        accountsListView.setAdapter(arrayAdapter);

        /**                                                                                      **
         * ------------------------------------------------------------------------------------- *
         */

    }

    @OnItemLongClick(R.id.accountsView)
    public boolean deleteAccount(int position) {
        String name = accountList.get(position).getName();
        accountManager.deleteAccount(name);
        Toast.makeText(this, "Account " + name + " deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    @OnItemClick(R.id.accountsView)
    public void chooseAccount(int position){
        String name = accountList.get(position).getName();
        accountManager.chooseAccount(name);
        Toast.makeText(this, accountManager.getActiveAccountName(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Poof! New identity! (Just kidding)", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.postLoginButton)
    public void onClick(View view) {
        // TODO: Save new user upon first login
        service.connect();

        shimmer.cancel();
            Intent selectedIntent = new Intent(LoginActivity.this, AuthUserActivity.class);
            startActivityForResult(selectedIntent, 0002);
    }

    @Override
    public void onConnected(final HttpClient httpClient) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d("HS_URL", HS_URL);
                    HttpGet get = new HttpGet(HS_URL);
                    String result = httpClient.execute(get, new BasicResponseHandler());

                    System.out.println(result);
                } catch (IOException e) {
                    onConnectionException(e);
                }
            }
        }.start();
    }

    @Override
    public void onConnectionException(Exception e) {
        Log.e(getClass().getSimpleName(),
                "Exception connecting to hidden service", e);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, R.string.msg_crash,
                                Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        });
    }

    @Override
    public void onTimeout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast
                        .makeText(LoginActivity.this, R.string.msg_timeout,
                                Toast.LENGTH_LONG)
                        .show();
                Log.d("onTimeout", String.valueOf(R.string.msg_timeout));
                finish();
            }
        });
    }

    @Override
    public void onInvalid() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast
                        .makeText(LoginActivity.this, R.string.msg_invalid,
                                Toast.LENGTH_LONG)
                        .show();
                Log.d("onInvalid", String.valueOf(R.string.msg_invalid));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
