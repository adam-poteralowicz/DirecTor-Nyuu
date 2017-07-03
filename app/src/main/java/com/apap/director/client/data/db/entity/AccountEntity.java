package com.apap.director.client.data.db.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountEntity extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private byte[] keyPair;
    private int registrationId;
    private String masterPassword;

    private String keyBase64;

    private RealmList<ContactEntity> contacts;
    private RealmList<OneTimeKeyEntity> oneTimeKeys;
    private SignedKeyEntity signedKey;
    private RealmList<SessionEntity> sessions;
    private boolean registered;
    private boolean active;
    private String cookie;



    public RealmList<ContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(RealmList<ContactEntity> contacts) {
        this.contacts = contacts;
    }

    public RealmList<OneTimeKeyEntity> getOneTimeKeys() {
        return oneTimeKeys;
    }

    public void setOneTimeKeys(RealmList<OneTimeKeyEntity> oneTimeKeys) {
        this.oneTimeKeys = oneTimeKeys;
    }

    public SignedKeyEntity getSignedKey() {
        return signedKey;
    }

    public void setSignedKey(SignedKeyEntity signedKey) {
        this.signedKey = signedKey;
    }

    public RealmList<SessionEntity> getSessions() {
        return sessions;
    }

    public void setSessions(RealmList<SessionEntity> sessions) {
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

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String password) {
        this.masterPassword = password;
    }

    @Override
    public String toString() {
        return name;
    }
}
