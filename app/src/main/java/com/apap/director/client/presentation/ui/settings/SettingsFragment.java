package com.apap.director.client.presentation.ui.settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static android.view.View.VISIBLE;

public class SettingsFragment extends Fragment {

    @BindView(R.id.settingsView_editText)
    EditText editText;

    @BindView(R.id.settingsView_confirmButton)
    Button confirmButton;

    AccountEntity account;
    private Realm realm;
    private String TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_view, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        account = realm.where(AccountEntity.class).equalTo("active", true).findFirst();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @OnClick(R.id.settingsView_masterPasswordButton)
    void changeMasterPassword() {
        editText.setVisibility(VISIBLE);
        confirmButton.setVisibility(VISIBLE);
    }

    @OnClick(R.id.settingsView_confirmButton)
    void confirmPasswordChanged() {
        realm.beginTransaction();
        account.setMasterPassword(String.valueOf(editText.getText()));
        realm.copyToRealmOrUpdate(account);
        realm.commitTransaction();
        Log.v(TAG, "Master password changed");
    }
}
