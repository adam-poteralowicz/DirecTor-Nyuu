package com.apap.director.db.realm;


import android.util.Log;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;
import com.apap.director.db.realm.model.OneTimeKey;
import com.apap.director.db.realm.model.Session;
import com.apap.director.db.realm.model.SignedKey;
import com.apap.director.db.rest.service.UserService;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class AccountManager {

    private Realm realm;
    private UserService userService;

    public AccountManager(UserService userService) {
        this.realm = Realm.getDefaultInstance();
    }

    public ArrayList<Account> listAllAccounts(){
        RealmResults<Account> accounts = realm.where(Account.class).findAll();
        return new ArrayList<Account>(accounts);
    }

    public boolean createAccount(String name){

        Account sameName = realm.where(Account.class).equalTo("name", name).findFirst();
        if(sameName != null) return false;

        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        int registrationId  = KeyHelper.generateRegistrationId(false);

        realm.beginTransaction();
            Account account = realm.createObject(Account.class, generateAccountId());
            account.setKeyPair(identityKeyPair.serialize());
            account.setName(name);
            account.setRegistrationId(registrationId);
            realm.copyToRealmOrUpdate(account);
        realm.commitTransaction();

        return true;

    }

    public String getActiveAccountName(){
        Account activeAccount = realm.where(Account.class).equalTo("active", true).findFirst();
        return activeAccount==null ? null : activeAccount.getName();
    }

    public Account getActiveAccount(){
        return realm.where(Account.class).equalTo("active", true).findFirst();
    }

    public boolean chooseAccount(String name){

        Account anyAccount = realm.where(Account.class).equalTo("name", name).findFirst();

        if(anyAccount == null) return false;

        realm.beginTransaction();
            RealmResults<Account> activeAccounts = realm.where(Account.class).equalTo("active", true).findAll();

            for(Account account : activeAccounts){
                account.setActive(false);
            }

            Account chosenAccount = realm.where(Account.class).equalTo("name", name).findFirst();
            chosenAccount.setActive(true);

        realm.commitTransaction();

        return true;
    }

    public boolean deleteAccount(String name){

        Account sameName = realm.where(Account.class).equalTo("name", name).findFirst();
        if(sameName == null) return false;

        realm.beginTransaction();
            RealmResults<OneTimeKey> oneTimeKeys = realm.where(OneTimeKey.class).equalTo("account.name", name).findAll();
            oneTimeKeys.deleteAllFromRealm();

            RealmResults<Message> messages = realm.where(Message.class).equalTo("account.name", name).findAll();
            messages.deleteAllFromRealm();

            RealmResults<SignedKey> signedKeys = realm.where(SignedKey.class).equalTo("account.name", name).findAll();
            signedKeys.deleteAllFromRealm();

            RealmResults<Session> sessions = realm.where(Session.class).equalTo("account.name", name).findAll();
            sessions.deleteAllFromRealm();

            RealmResults<Conversation> conversations = realm.where(Conversation.class).equalTo("account.name", name).findAll();
            conversations.deleteAllFromRealm();

            RealmResults<ContactKey> contactKeys = realm.where(ContactKey.class).equalTo("account.name", name).findAll();
            contactKeys.deleteAllFromRealm();

            realm.where(Account.class).equalTo("name", name).findFirst().deleteFromRealm();
        realm.commitTransaction();
        Log.d("DTOR", "DELETING ACCOUNT "+name);
        Log.d("DTOR/Accounts", realm.where(Account.class).findAll().toString());

        return true;
    }

    /***
     * Logs in as active account
     * @return success
     */

    public boolean logIn(){
        return false;
    }

    /**
     *
     * @return id for new Realm Account object
     */
    private long generateAccountId() {
        long id;
        try {
            if (realm.where(Account.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(Account.class).max("id").longValue() + 1;
            }
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }



}
