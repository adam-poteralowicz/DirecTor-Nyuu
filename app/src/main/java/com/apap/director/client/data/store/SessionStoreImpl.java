package com.apap.director.client.data.store;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactKeyEntity;
import com.apap.director.client.data.db.entity.SessionEntity;

import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;


public class SessionStoreImpl implements SessionStore {

    private Realm realm;

    @Inject
    public SessionStoreImpl(Realm realm) {
        this.realm = realm;
    }

    @Override
    public SessionRecord loadSession(SignalProtocolAddress address) {

        Realm realm = Realm.getDefaultInstance();

        try {
            SessionEntity session = realm.where(SessionEntity.class)
                    .equalTo("name", address.getName())
                    .equalTo("deviceId", address.getDeviceId())
                    .findFirst();

            return session == null ? null : new SessionRecord(session.getSerializedKey());
        } catch (IOException e) {
            Log.getStackTraceString(e);
            return null;
        }
        finally {
            realm.close();
        }
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        List<SessionEntity> list = realm.where(SessionEntity.class).equalTo("name", name).findAll();
        List<Integer> subDeviceSessions = new ArrayList<>(list.size());

        for (SessionEntity session : list) {
            subDeviceSessions.add(session.getDeviceId());
        }

        return subDeviceSessions;
    }

    @Override
    public void storeSession(SignalProtocolAddress address, SessionRecord record) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

            SessionEntity sameName = realm.where(SessionEntity.class).equalTo("name", address.getName()).equalTo("deviceId", address.getDeviceId()).findFirst();
            if (sameName != null) {
                sameName.setSerializedKey(record.serialize());
                realm.copyToRealmOrUpdate(sameName);
                realm.insertOrUpdate(sameName);
                realm.commitTransaction();
                realm.close();
                return;
            }

            long id = 0;

            if (realm.where(SessionEntity.class).findFirst() != null) {
                id = realm.where(SessionEntity.class).max("id").longValue() + 1;
            }

            SessionEntity session = realm.createObject(SessionEntity.class, id);
            session.setAccount(realm.where(AccountEntity.class).equalTo("active", true).findFirst());
            session.setName(address.getName());
            ContactKeyEntity contactKey = realm.where(ContactKeyEntity.class).equalTo("keyBase64", address.getName()).findFirst();
            session.setConversation(contactKey.getContact().getConversation());
            session.setDeviceId(address.getDeviceId());
            session.setSerializedKey(record.serialize());

        realm.commitTransaction();
        realm.close();

    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {
        Realm realm = Realm.getDefaultInstance();
        SessionEntity session = realm.where(SessionEntity.class)
                .equalTo("name", address.getName())
                .equalTo("deviceId", address.getDeviceId())
                .findFirst();

        realm.close();
        return session != null;

    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            realm.where(SessionEntity.class).equalTo("deviceId", address.getDeviceId()).equalTo("name", address.getName()).findFirst().deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void deleteAllSessions(String name) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            realm.where(SessionEntity.class).equalTo("name", name).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }
}
