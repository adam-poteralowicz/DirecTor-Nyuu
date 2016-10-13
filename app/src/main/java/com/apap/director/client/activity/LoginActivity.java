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

import com.apap.director.client.R;
import com.apap.director.im.domain.chat.service.TCPChatService;
import com.apap.director.im.util.SimpleBinder;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public class LoginActivity extends Activity {

    TCPChatService chatService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        EditText usernameField = (EditText) findViewById(R.id.username);
        usernameField.setHint("Username");
        EditText serverField = (EditText) findViewById(R.id.server);
        serverField.setHint("Server");
        EditText portField = (EditText) findViewById(R.id.port);
        portField.setHint("Port");

    }

    public void onClick(View view) {
        // TODO: Save new user upon first login

        if (view.getId() == R.id.postLoginButton) {

            ServiceConnection connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.v("HAI/ServiceConnection", "Connected");
                    Log.v("HAI", "HAI HAI HAI");
                    SimpleBinder binder = (SimpleBinder) service;
                    chatService = (TCPChatService) binder.getService();



                    chatService.connect("dev02.sagiton.pl",5222);

                    Intent selectedIntent = new Intent(LoginActivity.this, AuthUserActivity.class);
                    startActivityForResult(selectedIntent, 0002);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.v("HAIServiceConnection", "Disonnected");
                }

            };

            Log.v("HAI/LoginActivity", "Trying to bind...");
            Intent intent = new Intent(this, TCPChatService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);


        }

    }
}
