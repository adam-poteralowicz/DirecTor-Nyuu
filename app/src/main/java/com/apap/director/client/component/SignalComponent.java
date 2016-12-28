package com.apap.director.client.component;

import com.apap.director.client.activity.LoginActivity;
import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.im.signal.module.ApplicationScope;
import com.apap.director.im.websocket.module.WebSocketModule;
import com.apap.director.im.websocket.service.StompService;
import com.apap.director.im.signal.module.SignalModule;

import javax.inject.Singleton;

import dagger.Component;

@ApplicationScope
@Component(modules = {SignalModule.class, WebSocketModule.class}, dependencies = {DaoComponent.class})
public interface SignalComponent {

    void inject(StompService stompService);
    void inject(LoginActivity loginActivity);
}
