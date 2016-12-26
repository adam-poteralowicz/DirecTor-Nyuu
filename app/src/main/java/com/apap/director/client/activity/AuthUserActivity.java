package com.apap.director.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.apap.director.client.R;
import com.apap.director.client.adapter.DirecTorPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthUserActivity extends FragmentActivity {
    DirecTorPagerAdapter direcTorPagerAdapter;
    @BindView(R.id.pager) ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);
        ButterKnife.bind(this);

        direcTorPagerAdapter = new DirecTorPagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(direcTorPagerAdapter);
    }


//    @OnClick(R.id.addNewContactButton)
//    public void onClick(View view) {
//            Intent selectedIntent = new Intent(AuthUserActivity.this, AddContactActivity.class);
//            startActivityForResult(selectedIntent, 0010);
//    }
}

