package com.apap.director.db.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Account extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private byte[] keyPair;
    private int registrationId;

    private RealmList<Contact> contacts;
    private RealmList<OneTimeKey> oneTimeKeys;
    private SignedKey signedKey;
    private RealmList<Session> sessions;


    public Account() {
    }

    public long getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(byte[] keyPair) {
        this.keyPair = keyPair;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public RealmList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(RealmList<Contact> contacts) {
        this.contacts = contacts;
    }

    public RealmList<OneTimeKey> getOneTimeKeys() {
        return oneTimeKeys;
    }

    public void setOneTimeKeys(RealmList<OneTimeKey> oneTimeKeys) {
        this.oneTimeKeys = oneTimeKeys;
    }

    public SignedKey getSignedKey() {
        return signedKey;
    }

    public void setSignedKey(SignedKey signedKey) {
        this.signedKey = signedKey;
    }

    public RealmList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(RealmList<Session> sessions) {
        this.sessions = sessions;
    }
}
