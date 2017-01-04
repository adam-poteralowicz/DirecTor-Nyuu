package com.apap.director.manager;


import android.util.Base64;
import android.util.Log;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;
import com.apap.director.db.realm.model.OneTimeKey;
import com.apap.director.db.realm.model.Session;
import com.apap.director.db.realm.model.SignedKey;
import com.apap.director.network.rest.service.LoginDetails;
import com.apap.director.network.rest.service.UserService;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.util.ByteUtil;
import org.whispersystems.libsignal.util.KeyHelper;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountManager {

    private Realm realm;
    private UserService userService;
    private Curve25519 curve25519;

    @Inject
    public AccountManager(Realm realm, UserService userService, Curve25519 curve25519) {
        this.realm = realm;
        this.userService = userService;
        this.curve25519 = curve25519;
    }

    public ArrayList<Account> listAllAccounts(){
        RealmResults<Account> accounts = realm.where(Account.class).findAll();
        return new ArrayList<Account>(accounts);
    }

    public Account createAccount(String name){

        Account sameName = realm.where(Account.class).equalTo("name", name).findFirst();
        if(sameName != null) return null;

        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        int registrationId  = KeyHelper.generateRegistrationId(false);


        Account account = new Account();
        account.setId(generateAccountId());
        account.setKeyPair(identityKeyPair.serialize());
        account.setName(name);
        Log.v("HAI/AccountManager", "Length before split "+identityKeyPair.getPublicKey().serialize().length);
        byte[][] typeAndKey = ByteUtil.split(identityKeyPair.getPublicKey().serialize(), 1, 32);
        account.setKeyBase64(Base64.encodeToString(typeAndKey[1], Base64.URL_SAFE | Base64.NO_WRAP));
        account.setRegistrationId(registrationId);

        realm.beginTransaction();
            realm.copyToRealmOrUpdate(account);
        realm.commitTransaction();

        return account;

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

        Log.v("HAI/AccountManager", "DELETING ACCOUNT "+name);
        Log.v("HAI/AccountManager", realm.where(Account.class).findAll().toString());

        return true;
    }

    /***
     * Logs in as active account
     * @return success
     */

    public void signUp(final Account account){

        Call<ResponseBody> call = userService.signUp(account.getKeyBase64());
        Log.v("HAI/AccountManager", "Sign up call to : " + call.request().url());
        Log.v("HAI/AccountManager", "Sign up method : " + call.request().method());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.v("HAI/AccountManager", "Sign up response: " + response.message());
                Log.v("HAI/AccountManager", "Sign up response code: " + response.code());

                if(response.isSuccessful()) {
                    realm.beginTransaction();
                        account.setRegistered(true);
                        realm.insertOrUpdate(account);
                    realm.commitTransaction();
                    Log.v("HAI/AccountManager", "Account " + account.getName() + " registered");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("HAI/AccountManager", "Account "+account.getName()+" failed to sign up");
            }
        });

    }

    public boolean logIn(){

        try {

            Realm realm = Realm.getDefaultInstance();

            Account active = realm.where(Account.class).equalTo("active", true).equalTo("registered", true).findFirst();
            if(active == null) return false;

            Call<String> requestCodeCall = userService.requestCode(active.getKeyBase64());
            Response<String> codeCallResponse = requestCodeCall.execute();

            Log.v("HAI/AccountManager", "request code url " + requestCodeCall.request().url());

            if(!codeCallResponse.isSuccessful()){
                Log.v("HAI/AccountManager", "Failed to fetch code");
                return false;
            }

            Log.v("HAI/AccountManager", "Fetched code " + codeCallResponse.body());
            Log.v("HAI/AccountManager", "Fetching status " + codeCallResponse.code());

            IdentityKeyPair keyPair = new IdentityKeyPair(active.getKeyPair());

            byte[] signature = curve25519.calculateSignature(keyPair.getPrivateKey().serialize(), codeCallResponse.body().getBytes());

            Log.v("HAI/AccountManager", new String(signature));



            LoginDetails loginDetails = new LoginDetails(active.getKeyBase64(), Base64.encodeToString(signature, Base64.URL_SAFE | Base64.NO_WRAP));



            Call<ResponseBody> loginCall = userService.login(loginDetails);
            Response<ResponseBody> loginCallResponse = loginCall.execute();

            Log.v("HAI/AccountManager", "login call code:" + loginCallResponse.code());
            Log.v("HAI/AccountManager", "login call account:" + new String(Base64.decode(active.getKeyBase64(), Base64.NO_WRAP | Base64.URL_SAFE), 32));

            realm.close();

            if(!loginCallResponse.isSuccessful()){
                Log.v("HAI/AccountManager", "Failed to login");
                return false;
            }

            Log.v("HAI/AccountManager", "Login successful");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }

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
