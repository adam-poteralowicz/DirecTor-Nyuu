package com.apap.director.im;


import android.util.Base64;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.Message;
import com.apap.director.db.realm.model.OneTimeKey;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class AccountManager {

    private Realm realm;

    public AccountManager() {
        this.realm = Realm.getDefaultInstance();
    }

    public boolean createAccount(String name){

        Account sameName = realm.where(Account.class).equalTo("name", name).findFirst();
        if(sameName != null) return false;

        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        int registrationId  = KeyHelper.generateRegistrationId(false);

        realm.beginTransaction();
            Account account = realm.createObject(Account.class);
            account.setKeyPair(identityKeyPair.serialize());
            account.setName(name);
            account.setRegistrationId(registrationId);
        realm.commitTransaction();

        return true;

    }

    public boolean deleteAccount(String name){
        realm.beginTransaction();
            RealmResults<OneTimeKey> oneTimeKeys = realm.where(OneTimeKey.class).equalTo("account.name", name).findAll();
            oneTimeKeys.deleteAllFromRealm();
        realm.commitTransaction();

        return false;
    }


}
