package com.apap.director.im.signal;

import com.apap.director.db.realm.model.Session;

import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;


public class DirectorSessionStore implements SessionStore {

    private Realm realm;

    @Inject
    public DirectorSessionStore(Realm realm) {
        this.realm = realm;
    }

    @Override
    public SessionRecord loadSession(SignalProtocolAddress address) {
        try {
            Session session = realm.where(Session.class)
                    .equalTo("name", address.getName())
                    .equalTo("deviceId", address.getDeviceId())
                    .findFirst();

            return new SessionRecord(session.getSerializedKey());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        List<Session> list = realm.where(Session.class).equalTo("name", name).findAll();
        List<Integer> subDeviceSessions = new ArrayList<>(list.size());

        for(Session session : list){
            subDeviceSessions.add(session.getDeviceId());
        }

        return subDeviceSessions;
    }

    @Override
    public void storeSession(SignalProtocolAddress address, SessionRecord record) {

        realm.beginTransaction();
            Session session = realm.createObject(Session.class);
            session.setName(address.getName());
            session.setDeviceId(address.getDeviceId());
            session.setSerializedKey(record.serialize());
        realm.commitTransaction();

    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {

        Session session =  realm.where(Session.class)
                .equalTo("name", address.getName())
                .equalTo("deviceId", address.getDeviceId())
                .findFirst();

        return session == null? false : true;

    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        realm.beginTransaction();
            realm.where(Session.class).equalTo("deviceId", address.getDeviceId()).equalTo("name", address.getName()).findFirst().deleteFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void deleteAllSessions(String name) {
        realm.beginTransaction();
            realm.where(Session.class).equalTo("name", name).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
