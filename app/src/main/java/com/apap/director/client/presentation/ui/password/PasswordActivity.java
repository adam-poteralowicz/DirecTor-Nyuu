package com.apap.director.client.presentation.ui.password;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.apap.director.client.R;
import com.apap.director.client.presentation.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Adam Poterałowicz
 */

public class PasswordActivity extends Activity {

    private static final int LAYOUT_ID = R.layout.password_activity_layout;
    private static final String SHARED_PREFERENCES_NAME = "prefs";

    @BindView(R.id.passwordActivity_editText)
    EditText editText;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);

        ButterKnife.bind(this);
        prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
    }

    @OnClick(R.id.passwordActivity_confirmButton)
    public void onClick() {
        String masterPassword = String.valueOf(editText.getText());
        Log.v(PasswordActivity.class.getSimpleName(), "Master password set to " +masterPassword);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("masterPassword", masterPassword);
        editor.apply();


        startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
    }
}
