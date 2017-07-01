package com.apap.director.client.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.adapter.DirecTorPagerAdapter;
import com.apap.director.db.realm.model.Account;
import com.apap.director.manager.AccountManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AuthUserActivity extends FragmentActivity {

    @Inject
    AccountManager
            accountManager;

    @BindView(R.id.pager)
    ViewPager viewPager;

    DirecTorPagerAdapter direcTorPagerAdapter;
    Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ((App) getApplication()).getComponent().inject(this);
        setContentView(R.layout.user_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        direcTorPagerAdapter = new DirecTorPagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(direcTorPagerAdapter);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onBackPressed() {
        realm.beginTransaction();
        if (accountManager == null)
            Log.d("accountManager", "null");
        if (accountManager.getActiveAccount() == null)
            Log.d("active account", "null");
        Account active = accountManager.getActiveAccount();
        active.setActive(false);
        realm.copyToRealmOrUpdate(active);
        realm.commitTransaction();
        Intent selectedIntent = new Intent(AuthUserActivity.this, LoginActivity.class);
        startActivity(selectedIntent);
    }

}

