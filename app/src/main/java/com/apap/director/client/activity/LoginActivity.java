package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.util.ArrayAdapterChangeListener;
import com.apap.director.im.websocket.service.ClientService;
import com.apap.director.im.websocket.service.MessageAction;
import com.apap.director.manager.AccountManager;
import com.apap.director.network.rest.service.KeyService;
import com.apap.director.network.rest.service.UserService;
import com.apap.director.signal.DirectorIdentityKeyStore;
import com.apap.director.signal.DirectorPreKeyStore;
import com.apap.director.signal.DirectorSessionStore;
import com.apap.director.signal.DirectorSignedPreKeyStore;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.state.PreKeyStore;
import org.whispersystems.libsignal.state.SessionStore;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import info.guardianproject.netcipher.client.StrongBuilder;
import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {

    Shimmer shimmer;
    //    String HS_URL = "http://3zk5ak4bcbfvwgha.onion";
    String HS_URL = "http://www.wp.pl/static.html";

    @Inject
    AccountManager accountManager;
    @Inject
    UserService userService;
    @Inject
    Realm realm;

    @BindView(R.id.accountsView)
    ListView accountsListView;

    private ArrayList<Account> accountList;
    private RealmResults<Account> realmAccounts;
    private ArrayAdapterChangeListener<Account, RealmResults<Account>> listener;
    private ArrayAdapter<Account> arrayAdapter;
    private String newAccName;

    @Inject
    DirectorIdentityKeyStore identityKeyStore;

    @Inject
    DirectorSignedPreKeyStore signedPreKeyStore;

    @Inject
    DirectorPreKeyStore preKeyStore;

    @Inject
    DirectorSessionStore sessionStore;

    @Inject
    MessageAction messageAction;

    @Inject
    KeyService keyService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        ClientService.init(messageAction, sessionStore, identityKeyStore, preKeyStore, signedPreKeyStore, keyService);
        shimmer();

        realmAccounts = realm.where(Account.class).findAll();

        accountList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(
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
    public void onBackPressed() {
        ClientService.disconnect();
    }

    @OnItemLongClick(R.id.accountsView)
    public boolean deleteAccount(int position) {
        String name = accountList.get(position).getName();
        boolean deleted = accountManager.deleteAccount(name);
        if (!deleted)
            Log.v(getClass().getSimpleName(), "Account " + name + " failed to delete itself");
        else Log.v(getClass().getSimpleName(), "Account " + name + " deleted");
        Toast.makeText(this, "Account " + name + " deleted", Toast.LENGTH_LONG).show();
        return false;
    }

    @OnItemClick(R.id.accountsView)
    public void chooseAccount(int position) {
        boolean success = accountManager.chooseAccount(accountList.get(position).getName());
        Toast.makeText(this, accountManager.getActiveAccountName() + " " + success, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tor_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.swapId) {
            Log.v(getClass().getSimpleName(), "Swap Id pressed");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            newAccName = data.getStringExtra("accountName");
            Account testAccount = accountManager.createAccount(newAccName);
            accountManager.signUp(testAccount);
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
                        Log.v("HAI/" + getClass().getSimpleName(), "Trying to post keys");
                        accountManager.postSignedKey();
                        accountManager.postOneTimeKeys();

                    } catch (IOException e) {
                        Log.getStackTraceString(e);
                    }
                    return null;
                }
            };

            keysTask.execute().get();

            Log.v(getClass().getSimpleName(), "Choosen account: " + accountManager.getActiveAccount() + " cookie" + accountManager.getActiveAccount().getCookie());

            realm.beginTransaction();
            Account account = realm.where(Account.class).equalTo("active", true).findFirst();
            String cookie2 = account.getCookie();
            Log.v(getClass().getSimpleName(), "cookie2 " + cookie2);
            realm.commitTransaction();

            ClientService.connect(cookie);
            shimmer.cancel();
            startActivity(new Intent(LoginActivity.this, AuthUserActivity.class));

        } catch (InterruptedException | ExecutionException e) {
            Log.getStackTraceString(e);
        }

    }

    @OnClick(R.id.newAccButton)
    public void newAccount(View view) {
        Intent newAccIntent = new Intent(LoginActivity.this, NewAccountActivity.class);
        startActivityForResult(newAccIntent, 1);
    }

    private void shimmer() {
        ShimmerTextView shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_tv);
        shimmerTextView.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{}
                },
                new int[]{
                        Color.argb(255, 102, 102, 255)
                }
        ));
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
        getSupportActionBar().show();
    }
}