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
import com.apap.director.db.realm.to.OneTimeKeyTO;
import com.apap.director.db.realm.to.SignedKeyTO;
import com.apap.director.network.rest.service.KeyService;
import com.apap.director.network.rest.service.LoginDetails;
import com.apap.director.network.rest.service.UserService;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.util.ByteUtil;
import org.whispersystems.libsignal.util.KeyHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private KeyService keyService;

    @Inject
    public AccountManager(Realm realm, UserService userService, Curve25519 curve25519, KeyService keyService) {
        this.realm = realm;
        this.userService = userService;
        this.curve25519 = curve25519;
        this.keyService = keyService;
    }

    public ArrayList<Account> listAllAccounts(){
        RealmResults<Account> accounts = realm.where(Account.class).findAll();
        return new ArrayList<>(accounts);
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
                realm.copyToRealmOrUpdate(account);
            }

            Account chosenAccount = realm.where(Account.class).equalTo("name", name).findFirst();
            chosenAccount.setActive(true);
            realm.copyToRealmOrUpdate(chosenAccount);
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
                Log.v("HAI", t.getMessage() + " " +t.getCause() + " ");
                t.printStackTrace();
            }
        });

    }

    public String logIn(){

        try {

            Realm realm = Realm.getDefaultInstance();

            Account active = realm.where(Account.class).equalTo("active", true).equalTo("registered", true).findFirst();
            if(active == null) return null;

            Call<String> requestCodeCall = userService.requestCode(active.getKeyBase64());
            Response<String> codeCallResponse = requestCodeCall.execute();

            Log.v("HAI/AccountManager", "request code url " + requestCodeCall.request().url());

            if(!codeCallResponse.isSuccessful()){
                Log.v("HAI/AccountManager", "Failed to fetch code");
                return null;
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

            for(String name: loginCallResponse.headers().names()){
                Log.v("HAI/AccountManager", name+" : "+ loginCallResponse.headers().get(name));
            }

            String cookie = loginCallResponse.headers().get("Set-Cookie").split(";")[0];

            realm.beginTransaction();
                Account active2 = realm.where(Account.class).equalTo("active", true).findFirst();
                Log.v("HAI/AccountManager", "Cookie "+cookie);
                active2.setCookie(cookie);
                realm.copyToRealmOrUpdate(active2);
            realm.commitTransaction();


            if(!loginCallResponse.isSuccessful()){
                Log.v("HAI/AccountManager", "Failed to login");
                return null;
            }

            Log.v("HAI/AccountManager", "Login successful");
            realm.close();
            return cookie;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String updateKeys() {
        try {
            Realm realm = Realm.getDefaultInstance();
            Account active = realm.where(Account.class).equalTo("active", true).equalTo("registered", true).findFirst();

            // postOneTimeKeys(keys)
            if (active.getOneTimeKeys() == null || active.getOneTimeKeys().isEmpty()) {
                Log.d("HAI/AccountManager", "There are no one time keys to be fetched");
                return null;
            }
            List<OneTimeKey> otkeys = new ArrayList<>(active.getOneTimeKeys());
            List<OneTimeKeyTO> otkeysTO = new ArrayList<>();
            for (OneTimeKey otk : otkeys) {
                otkeysTO.add(new OneTimeKeyTO(otk));
            }
            Call<ResponseBody> postOneTimeKeysCall = keyService.postOneTimeKeys(otkeysTO);
            Response<ResponseBody> postOneTimeKeysResponse = postOneTimeKeysCall.execute();

            Log.v("HAI/AccountManager", "post one time keys call code:" + postOneTimeKeysResponse.code());
            Log.v("HAI/AccountManager", "post one time keys call keys:" + otkeysTO);

            for(String name: postOneTimeKeysResponse.headers().names()){
                Log.v("HAI/AccountManager", name+" : "+ postOneTimeKeysResponse.headers().get(name));
            }

            if(!postOneTimeKeysResponse.isSuccessful()){
                Log.v("HAI/AccountManager", "Failed to post one time keys");
                return null;
            }

            Log.v("HAI/AccountManager", "PostOneTimeKeys successful");

            // postSignedKey(key)
            if (active.getSignedKey() == null) {
                Log.d("HAI/AccountManager", "There are no signed keys to be fetched");
                return null;
            }
            List<SignedKey> sk = new ArrayList<SignedKey>();
            sk.add(active.getSignedKey());
            List<SignedKeyTO> signedKeyTO = new ArrayList<>();
            for (SignedKey signedKey : sk) {
                signedKeyTO.add(new SignedKeyTO(signedKey));
            }
            Call<ResponseBody> postSignedKeysCall = keyService.postSignedKeys(signedKeyTO);
            Response<ResponseBody> postSignedKeysResponse = postSignedKeysCall.execute();

            Log.v("HAI/AccountManager", "post signed keys call code:" + postSignedKeysResponse.code());
            Log.v("HAI/AccountManager", "post signed keys call keys:" + signedKeyTO);

            for(String name: postSignedKeysResponse.headers().names()){
                Log.v("HAI/AccountManager", name+" : "+ postSignedKeysResponse.headers().get(name));
            }

            if(!postSignedKeysResponse.isSuccessful()){
                Log.v("HAI/AccountManager", "Failed to post signed keys");
                return null;
            }

            Log.v("HAI/AccountManager", "PostSignedKeys successful");
            return "Success";


        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
