package com.apap.director.im.domain.chat.service;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

public class TCPChatService extends AbstractChatService {

    public TCPChatService() {
    }

    @Override
    public void connect(String host, int port) throws IOException, XMPPException, SmackException {

        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        this.connection = new XMPPTCPConnection(builder.setServiceName(host).setPort(port).build());

        super.connect(host, port);
    }

}
