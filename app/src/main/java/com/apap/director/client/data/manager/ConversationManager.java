package com.apap.director.client.data.manager;

import javax.inject.Inject;

import io.realm.Realm;

public class ConversationManager {
    private Realm realm;

    @Inject
    public ConversationManager(Realm realm) {
        this.realm = realm;
    }
}
