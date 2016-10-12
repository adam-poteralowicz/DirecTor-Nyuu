package com.apap.director.im.domain.connection.event;


import android.util.Log;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

public class ConnectionEventListener implements ConnectionListener {
    public void connected(XMPPConnection xmppConnection) {
        Log.i("ConnectionEventListener", "Connected");
    }

    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        Log.i("ConnectionEventListener", "Authenticated");
    }

    public void connectionClosed() {
        Log.i("ConnectionEventListener", "Closed");
    }

    public void connectionClosedOnError(Exception e) {
        Log.i("ConnectionEventListener", "Closed on error");
    }

    public void reconnectionSuccessful() {
        Log.i("ConnectionEventListener", "Reconnected");
    }

    public void reconnectingIn(int i) {

    }

    public void reconnectionFailed(Exception e) {

    }
}
