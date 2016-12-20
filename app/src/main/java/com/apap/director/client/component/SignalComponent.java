package com.apap.director.client.component;

import com.apap.director.db.dao.module.DaoModule;
import com.apap.director.im.domain.websocket.module.WebSocketModule;
import com.apap.director.im.domain.websocket.service.StompService;
import com.apap.director.im.signal.module.SignalModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SignalModule.class, WebSocketModule.class})
public interface SignalComponent {
    void inject(StompService stompService);
}
