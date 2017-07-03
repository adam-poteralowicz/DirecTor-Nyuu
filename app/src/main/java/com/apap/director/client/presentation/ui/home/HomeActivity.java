package com.apap.director.client.presentation.ui.home;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.apap.director.client.R;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.presentation.ui.home.adapter.DirecTorPagerAdapter;
import com.apap.director.client.presentation.ui.home.di.component.DaggerHomeComponent;
import com.apap.director.client.presentation.ui.home.presenter.HomePresenter;
import com.apap.director.client.presentation.ui.login.LoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class HomeActivity extends FragmentActivity {

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
        DaggerHomeComponent.builder().build().inject(this);
        ButterKnife.bind(this);

        direcTorPagerAdapter = new DirecTorPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(direcTorPagerAdapter);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onBackPressed() {
        homePresenter.logOut(accountManager.getActiveAccount());

        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

}

