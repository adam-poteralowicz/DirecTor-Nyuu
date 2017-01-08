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
import com.apap.director.manager.AccountManager;
import com.apap.director.network.rest.service.UserService;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

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

//import com.apap.director.manager.AccountManager;

public class LoginActivity extends AppCompatActivity {

    Shimmer shimmer;
    //String HS_URL = "http://3zk5ak4bcbfvwgha.onion";

    @BindView(R.id.accountsView)
    ListView accountsListView;

    @Inject
    Realm realm;

    private ArrayList<Account> accountList;
    private RealmResults<Account> realmAccounts;
    private ArrayAdapterChangeListener<Account, RealmResults<Account>> listener;
    private ArrayAdapter<Account> arrayAdapter;
    private String newAccName;

    @Inject
    AccountManager accountManager;

    @Inject
    UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        shimmer();

        /**
         * Account List View
         */
        realmAccounts = realm.where(Account.class).findAll();

        accountList = new ArrayList<Account>();
        arrayAdapter = new ArrayAdapter<Account>(
                getApplicationContext(),
                android.R.layout.simple_list_item_single_choice,
                accountList);
        accountsListView.setAdapter(arrayAdapter);

        arrayAdapter.addAll(realmAccounts);
        arrayAdapter.notifyDataSetChanged();

        listener = new ArrayAdapterChangeListener<>(arrayAdapter);
        realmAccounts.addChangeListener(listener);


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

    @Override
    public void onBackPressed() {
    }

    @OnItemLongClick(R.id.accountsView)
    public boolean deleteAccount(int position) {
        String name = accountList.get(position).getName();
        boolean deleted = accountManager.deleteAccount(name);
        if (deleted == false)
            Log.v("HAI/LoginActivity", "Account " + name + " failed to delete itself");
        else Log.v("HAI/LoginActivity", "Account " + name + " deleted");
        Toast.makeText(this, "Account " + name + " deleted", Toast.LENGTH_LONG).show();
        return false;
    }

    @OnItemClick(R.id.accountsView)
    public void chooseAccount(int position) {
        boolean success = accountManager.chooseAccount(accountList.get(position).getName());
        Toast.makeText(this, accountManager.getActiveAccountName() + " " +success, Toast.LENGTH_LONG).show();
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
                Log.v("HAI/LoginActivity", "Swap Id pressed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.postLoginButton)
    public void onClick(View view) {
        try {
            if (accountManager.getActiveAccount() == null) {
                Toast.makeText(this, "Choose an account", Toast.LENGTH_LONG).show();
                return;
            } else Log.d("active account", accountManager.getActiveAccountName());

            AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return accountManager.logIn();
                }
            };

            String cookie=asyncTask.execute().get();
            Log.v("HAI/LoginActivity", "Logged in");

            Log.v("HAI/LoginActivity", "Choosen account: "+accountManager.getActiveAccount()+ " cookie" +accountManager.getActiveAccount().getCookie());

            realm.beginTransaction();
            Account account = realm.where(Account.class).equalTo("active", true).findFirst();
            String cookie2 = account.getCookie();
            Log.v("HAI/LoginActivity", "cookie2 "+cookie2);
            realm.commitTransaction();

            ClientService.connect(cookie);
//            ClientService.sendMessage("LoginActivity");
            shimmer.cancel();
            Intent selectedIntent = new Intent(LoginActivity.this, AuthUserActivity.class);
            startActivity(selectedIntent);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.newAccButton)
    public void newAccount(View view) {
        Intent newAccIntent = new Intent(LoginActivity.this, NewAccountActivity.class);
        startActivityForResult(newAccIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                newAccName = data.getStringExtra("accountName");
                Account testAccount = accountManager.createAccount(newAccName);
                accountManager.signUp(testAccount);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmAccounts.removeChangeListener(listener);
    }
}