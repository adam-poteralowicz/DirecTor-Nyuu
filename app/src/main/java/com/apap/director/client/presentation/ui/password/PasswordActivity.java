package com.apap.director.client.presentation.ui.password;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.apap.director.client.R;
import com.apap.director.client.presentation.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Adam Poterałowicz
 */

public class PasswordActivity extends Activity {

    private static final int LAYOUT_ID = R.layout.password_activity_layout;

    @BindView(R.id.passwordActivity_editText)
    EditText editText;

    private SharedPreferences.Editor prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);
    }

    @OnClick(R.id.passwordActivity_confirmButton)
    public void onClick() {
        String masterPassword = String.valueOf(editText.getText());
        prefs.putString("masterPassword", masterPassword);
        prefs.commit();

        startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
    }
}
