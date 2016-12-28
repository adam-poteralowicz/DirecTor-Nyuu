package com.apap.director.im.signal;

import com.apap.director.db.dao.model.DbSession;
import com.apap.director.db.manager.DatabaseManager;

import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class DirectorSessionStore implements SessionStore {

    private DatabaseManager manager;

    @Inject
    public DirectorSessionStore(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public SessionRecord loadSession(SignalProtocolAddress address) {
        try {
            List<DbSession> list = manager.listDbSessionsByName(address.getName());
            for (DbSession session : list) {
                if (session.getDeviceId() == address.getDeviceId())
                    return new SessionRecord(session.getSerialized());
            }
            return null;
        }
        catch(IOException exception){
            return null;
        }
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        List<DbSession> list = manager.listDbSessionsByName(name);
        List<Integer> subDeviceSessions = new ArrayList<>(list.size());

        for(DbSession session : list){
            subDeviceSessions.add(session.getDeviceId());
        }

        return subDeviceSessions;
    }

    @Override
    public void storeSession(SignalProtocolAddress address, SessionRecord record) {

        DbSession session = new DbSession();
        session.setName(address.getName());
        session.setDeviceId(address.getDeviceId());
        session.setSerialized(record.serialize());

        manager.insertOrUpdateDbSession(session);

    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {

        List<DbSession> sessions = manager.listDbSessionsByName(address.getName());
        for(DbSession session : sessions){
            if(session.getDeviceId()==address.getDeviceId()) return true;
        }

        return false;

    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        manager.deleteDbSessionByDeviceIdAndName(address.getDeviceId(), address.getName());
    }

    @Override
    public void deleteAllSessions(String name) {
        manager.deleteDbSessionByName(name);
    }
}
