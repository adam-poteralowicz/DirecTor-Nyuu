package com.apap.director.client.presentation.ui.home;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.presentation.ui.home.adapter.DirecTorPagerAdapter;
import com.apap.director.client.presentation.ui.home.contract.HomeContract;
import com.apap.director.client.presentation.ui.home.di.component.DaggerHomeComponent;
import com.apap.director.client.presentation.ui.home.di.module.HomeContractModule;
import com.apap.director.client.presentation.ui.home.presenter.HomePresenter;
import com.apap.director.client.presentation.ui.login.LoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class HomeActivity extends FragmentActivity implements HomeContract.View {

    @Inject
    HomePresenter homePresenter;

    @Inject
    AccountManager accountManager;

    @BindView(R.id.pager)
    ViewPager viewPager;

    DirecTorPagerAdapter direcTorPagerAdapter;
    Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpInjection();
        ButterKnife.bind(this);

        direcTorPagerAdapter = new DirecTorPagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(direcTorPagerAdapter);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onBackPressed() {
        homePresenter.logOut(accountManager.getActiveAccount());

        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    @Override
    public void logOut(AccountEntity account) {
        realm.beginTransaction();
        account.setActive(false);
        realm.copyToRealmOrUpdate(account);
        realm.commitTransaction();
    }

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
    }

    private void setUpInjection() {
        DaggerHomeComponent.builder()
                .mainComponent(((App) getApplication()).getComponent())
                .homeContractModule(new HomeContractModule(this))
                .build()
                .inject(this);
    }
}

