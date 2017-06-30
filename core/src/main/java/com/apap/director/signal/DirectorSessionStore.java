package com.apap.director.signal;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.ContactKey;
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
            Realm realm = Realm.getDefaultInstance();
            Session session = realm.where(Session.class)
                    .equalTo("name", address.getName())
                    .equalTo("deviceId", address.getDeviceId())
                    .findFirst();

            return session == null? null : new SessionRecord(session.getSerializedKey());
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
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

            Session sameName = realm.where(Session.class).equalTo("name", address.getName()).equalTo("deviceId", address.getDeviceId()).findFirst();
            if(sameName != null){
                sameName.setSerializedKey(record.serialize());
                realm.copyToRealmOrUpdate(sameName);
                realm.insertOrUpdate(sameName);
                realm.commitTransaction();
                return;
            }

            long id = 0;

            if(realm.where(Session.class).findFirst()!=null){
                id = realm.where(Session.class).max("id").longValue()+1;
            }

            Session session = realm.createObject(Session.class, id);
            session.setAccount(realm.where(Account.class).equalTo("active", true).findFirst());
            session.setName(address.getName());
            ContactKey contactKey = realm.where(ContactKey.class).equalTo("keyBase64", address.getName()).findFirst();
            session.setConversation(contactKey.getContact().getConversation());
            session.setDeviceId(address.getDeviceId());
            session.setSerializedKey(record.serialize());
        realm.commitTransaction();

    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {
        Realm realm = Realm.getDefaultInstance();
        Session session =  realm.where(Session.class)
                .equalTo("name", address.getName())
                .equalTo("deviceId", address.getDeviceId())
                .findFirst();

        return session != null;

    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            realm.where(Session.class).equalTo("deviceId", address.getDeviceId()).equalTo("name", address.getName()).findFirst().deleteFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void deleteAllSessions(String name) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            realm.where(Session.class).equalTo("name", name).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
