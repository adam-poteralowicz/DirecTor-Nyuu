package com.apap.director.im.domain.chat.service;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public interface Chat {
    public void login(String username, String password) throws IOException, XMPPException, SmackException;
    public void connect(String host, String port) throws IOException, XMPPException, SmackException;
    public void sendMessage(String to, String message) throws SmackException.NotConnectedException;
}
