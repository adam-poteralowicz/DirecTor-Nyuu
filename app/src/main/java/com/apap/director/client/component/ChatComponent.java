package com.apap.director.client.component;


import com.apap.director.im.domain.chat.module.ChatModule;
import com.apap.director.im.domain.chat.service.TCPChatService;
import com.apap.director.im.domain.connection.module.ConnectionModule;
import com.apap.director.im.domain.message.event.MessageEventListener;
import com.apap.director.im.domain.message.module.MessageModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ChatModule.class, MessageModule.class, ConnectionModule.class})
public interface ChatComponent {
    void inject(TCPChatService chatService);
    MessageEventListener provideMessageEventListener();
}
