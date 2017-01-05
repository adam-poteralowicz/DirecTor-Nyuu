package com.apap.director.client.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.apap.director.client.R;
import com.apap.director.client.adapter.DirecTorPagerAdapter;
import com.apap.director.db.realm.model.Account;
import com.apap.director.manager.AccountManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class AuthUserActivity extends FragmentActivity {
    DirecTorPagerAdapter direcTorPagerAdapter;
    AccountManager accountManager;
    Realm realm;
    @BindView(R.id.pager) ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
            Account active = accountManager.getActiveAccount();
            active.setActive(false);
            realm.copyToRealmOrUpdate(active);
        realm.commitTransaction();
        Intent selectedIntent = new Intent(AuthUserActivity.this, LoginActivity.class);
        startActivity(selectedIntent);
    }

}

