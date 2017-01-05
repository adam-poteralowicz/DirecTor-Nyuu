package com.apap.director.db.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Account extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private byte[] keyPair;
    private int registrationId;

    private String keyBase64;

    private RealmList<Contact> contacts;
    private RealmList<OneTimeKey> oneTimeKeys;
    private SignedKey signedKey;
    private RealmList<Session> sessions;
    private boolean registered;
    private boolean active;
    private String cookie;



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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCookie(){
        return cookie;
    }

    public void setCookie(String cookie){
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return name;
    }
}
