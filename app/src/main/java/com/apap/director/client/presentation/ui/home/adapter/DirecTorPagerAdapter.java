package com.apap.director.client.presentation.ui.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.presentation.ui.contact.ContactsFragment;
import com.apap.director.client.presentation.ui.inbox.InboxFragment;
import com.apap.director.client.presentation.ui.settings.SettingsFragment;

public class DirecTorPagerAdapter extends FragmentPagerAdapter {

    private int mNumOfTabs;

    public DirecTorPagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.mNumOfTabs = numTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new ContactsFragment();
            case 1:
                return new InboxFragment();
            case 2:
                return new SettingsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        String contacts = App.getContext().getResources().getString(R.string.title_section_1).toUpperCase();
        String inbox = App.getContext().getResources().getString(R.string.title_section_2).toUpperCase();
        String settings = App.getContext().getResources().getString(R.string.settings).toUpperCase();

        if (position == 0) {
            return contacts;
        } else if (position == 1) {
            return inbox;
        } else if (position == 2) {
            return settings;
        }

        return null;
    }
}

