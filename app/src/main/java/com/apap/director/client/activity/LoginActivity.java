package com.apap.director.client.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.im.domain.chat.service.TCPChatService;
import com.apap.director.im.util.SimpleBinder;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public class LoginActivity extends Activity {

    TCPChatService chatService;
    String username, password, server;
    Integer port;
    EditText usernameField, passwordField, serverField, portField;
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

        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
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
}
