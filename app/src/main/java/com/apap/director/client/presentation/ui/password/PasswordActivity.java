package com.apap.director.client.presentation.ui.password;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.presentation.ui.home.HomeActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by Adam Poterałowicz
 */

public class PasswordActivity extends Activity {

    private static final int LAYOUT_ID = R.layout.password_activity_layout;

    @BindView(R.id.passwordActivity_editText)
    EditText editText;

    private final String TAG = this.getClass().getSimpleName();
    private AccountEntity account;
    private Realm realm;
    private SharedPreferences.Editor prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);

        realm = Realm.getDefaultInstance();
        account = realm.where(AccountEntity.class).equalTo("active", true).findFirst();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @OnClick(R.id.passwordActivity_confirmButton)
    public void onClick() {
        String masterPassword = String.valueOf(editText.getText());
        realm.beginTransaction();
        account.setMasterPassword(String.valueOf(masterPassword));
        realm.copyToRealmOrUpdate(account);
        realm.commitTransaction();

        Log.v(TAG, "Master password changed");
        prefs.putString("masterPassword", masterPassword);
        prefs.commit();

        startActivity(new Intent(PasswordActivity.this, HomeActivity.class));
    }
}
