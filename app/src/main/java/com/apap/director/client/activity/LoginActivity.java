package com.apap.director.client.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apap.director.client.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import info.guardianproject.netcipher.NetCipher;
import info.guardianproject.netcipher.client.StrongBuilder.Callback;
import info.guardianproject.netcipher.client.StrongHttpClientBuilder;

public class LoginActivity extends AppCompatActivity implements Callback<HttpClient> {

    EditText usernameField, passwordField, serverField, portField;
    Shimmer shimmer;
//    String HS_URL = "http://3zk5ak4bcbfvwgha.onion";
    String HS_URL = "http://www.wp.pl";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        try {
            StrongHttpClientBuilder builder = new StrongHttpClientBuilder(this);
            StrongHttpClientBuilder
                    .forMaxSecurity(this)
                    .withTorValidation()
                    .build(this);
            Log.d("Builder status", String.valueOf(builder.supportsHttpProxy())+"\n"+String.valueOf(builder.supportsSocksProxy())+"\n"+builder.toString());
        }
        catch (Exception e) {
            Toast.makeText(this, R.string.msg_crash, Toast.LENGTH_LONG)
                    .show();
            Log.e(getClass().getSimpleName(),
                    "Exception loading hidden service", e);
            finish();
        }

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

        getSupportActionBar().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tor_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tor_conn_on:
//                Context context = LoginActivity.this;
//                Intent mServiceIntent = new Intent(context, NetCipherService.class);
//                context.startService(mServiceIntent);
                NetCipher.useTor();
                return true;
            case R.id.tor_conn_off:
                NetCipher.clearProxy();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View view) {
        // TODO: Save new user upon first login

        if (view.getId() == R.id.postLoginButton) {

            shimmer.cancel();
            Intent selectedIntent = new Intent(LoginActivity.this, AuthUserActivity.class);
            startActivityForResult(selectedIntent, 0002);

//            ServiceConnection connection = new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName name, IBinder service) {
//                    Log.v("HAI/ServiceConnection", "Connected");
//                    Log.v("HAI", "HAI HAI HAI");
//                    SimpleBinder binder = (SimpleBinder) service;
//                    chatService = (TCPChatService) binder.getService();
//                    ((App) getApplication()).getChatComponent().inject(chatService);
//
//                    username = String.valueOf(usernameField.getText());
//                    password = String.valueOf(passwordField.getText());
//                    server = String.valueOf(serverField.getText());
//                    port = 5222;
//
//                    chatService.connect(server, port);
//                    chatService.login(username, password);
//
//
//                    Log.v("HAI/LoginActivity", "WAITED");
//                    chatService.sendMessage("ala@ALA-PC", "hai from app");

//                }

//                @Override
//                public void onServiceDisconnected(ComponentName name) {
//                    Log.v("HAIServiceConnection", "Disonnected");
//                }
//
//            };

//            Log.v("HAI/LoginActivity", "Trying to bind...");
//            Intent intent = new Intent(this, TCPChatService.class);
//            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onConnected(final HttpClient httpClient) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d("HS_URL", HS_URL);
                    HttpGet get = new HttpGet(HS_URL);
                    String result = httpClient.execute(get, new BasicResponseHandler());

                    System.out.println(result);
                } catch (IOException e) {
                    onConnectionException(e);
                }
            }
        }.start();
    }

    @Override
    public void onConnectionException(Exception e) {
        Log.e(getClass().getSimpleName(),
                "Exception connecting to hidden service", e);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, R.string.msg_crash,
                                Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        });
    }

    @Override
    public void onTimeout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast
                        .makeText(LoginActivity.this, R.string.msg_timeout,
                                Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        });
    }

    @Override
    public void onInvalid() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast
                        .makeText(LoginActivity.this, R.string.msg_invalid,
                                Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        });
    }
}
