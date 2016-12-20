package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.apap.director.client.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class LoginActivity extends Activity {


    EditText usernameField, passwordField, serverField;
    Shimmer shimmer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        usernameField = (EditText) findViewById(R.id.username);
        usernameField.setHint("Username");
        passwordField = (EditText) findViewById(R.id.password);
        passwordField.setHint("Password");
        serverField = (EditText) findViewById(R.id.server);
        serverField.setHint("Server");
        ShimmerTextView shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_tv);
        shimmerTextView.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{}
                },
                new int[] {
                        Color.argb(255, 102, 102, 255)
                }
        ));
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);

    }

    public void onClick(View view) {
        // TODO: Save new user upon first login

        if (view.getId() == R.id.postLoginButton) {

            shimmer.cancel();
            Intent selectedIntent = new Intent(LoginActivity.this, AuthUserActivity.class);
            startActivityForResult(selectedIntent, 0002);

        }
    }
}
