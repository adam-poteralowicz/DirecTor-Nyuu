package com.apap.director.im.domain.chat.service;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

/**
 * Created by Ala on 12/10/2016.
 */
public class TCPChatService extends AbstractChatService {

    public TCPChatService(){
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        this.connection = new XMPPTCPConnection(builder.build());
    }

}
