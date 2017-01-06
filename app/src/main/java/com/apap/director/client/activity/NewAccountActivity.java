package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.apap.director.client.App;
import com.apap.director.client.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewAccountActivity extends Activity {

    EditText accountNameEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_acc_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        accountNameEditText = (EditText) findViewById(R.id.accNameEditText);
        accountNameEditText.setHint("New account name");
    }

    @OnClick(R.id.saveAccButton)
    public void saveAccount() {
        String accountName = String.valueOf(accountNameEditText.getText());

        Intent newAccIntent = new Intent(NewAccountActivity.this, LoginActivity.class);
        newAccIntent.putExtra("accountName", accountName);
        Log.d("DTOR-NewAccount", accountName);
        if (!"".equals(accountName)) {
            setResult(Activity.RESULT_OK, newAccIntent);
            finish();
        }
    }

}
