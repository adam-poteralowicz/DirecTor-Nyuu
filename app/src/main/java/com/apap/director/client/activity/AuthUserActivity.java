package com.apap.director.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.apap.director.client.R;
import com.apap.director.client.adapter.DirecTorPagerAdapter;
import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.db.manager.IDatabaseManager;

public class AuthUserActivity extends FragmentActivity {
    DirecTorPagerAdapter direcTorPagerAdapter;
    ViewPager viewPager;

    /**
     * Manages the database for this application
     */
    private IDatabaseManager databaseManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        // init database manager
        databaseManager = new DatabaseManager(this);

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

    /**
     * Called after your activity has been stopped, prior to it being started again.
     * Always followed by onStart()
     */
    @Override
    protected void onRestart() {
        if (databaseManager == null)
            databaseManager = new DatabaseManager(this);

        super.onRestart();
    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity
     * to start interacting with the user.
     */
    @Override
    protected void onResume() {
        // init database manager
        databaseManager = DatabaseManager.getInstance(this);

        super.onResume();
    }

    /**
     * Called when you are no longer visible to the user.
     */
    @Override
    protected void onStop() {
        if (databaseManager != null)
            databaseManager.closeDbConnections();

        super.onStop();
    }
}

