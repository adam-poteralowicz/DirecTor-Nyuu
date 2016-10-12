package com.apap.director.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.apap.director.client.R;
import com.apap.director.client.adapter.DirecTorPagerAdapter;

public class AuthUserActivity extends FragmentActivity {
    DirecTorPagerAdapter direcTorPagerAdapter;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        direcTorPagerAdapter = new DirecTorPagerAdapter(getSupportFragmentManager(), 2);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(direcTorPagerAdapter);
    }


    public void onClick(View view) {

        if (view.getId() == R.id.addNewContactButton) {
            Intent selectedIntent = new Intent(AuthUserActivity.this, AddContactActivity.class);
            startActivityForResult(selectedIntent, 0010);
        }
    }

}

