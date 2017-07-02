package com.apap.director.client.data.manager;

import android.util.Base64;
import android.util.Log;

import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.LoginDetails;
import com.apap.director.client.data.net.rest.service.UserService;
import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.data.store.DirectorPreKeyStore;
import com.apap.director.client.data.store.DirectorSignedPreKeyStore;
import com.apap.director.client.domain.model.Account;
import com.apap.director.client.domain.model.ContactKey;
import com.apap.director.client.domain.model.Conversation;
import com.apap.director.client.domain.model.Message;
import com.apap.director.client.domain.model.OneTimeKey;
import com.apap.director.client.domain.model.Session;
import com.apap.director.client.domain.model.SignedKey;


import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.ByteUtil;
import org.whispersystems.libsignal.util.KeyHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
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
    private DirectorPreKeyStore preKeyStore;
    private DirectorSignedPreKeyStore signedPreKeyStore;
    private String TAG = this.getClass().getSimpleName();

    public AccountManager(Realm realm, UserService userService, Curve25519 curve25519, KeyService keyService, DirectorPreKeyStore preKeyStore, DirectorSignedPreKeyStore signedPreKeyStore) {
        this.realm = realm;
        this.userService = userService;
        this.curve25519 = curve25519;
        this.keyService = keyService;
        this.preKeyStore = preKeyStore;
        this.signedPreKeyStore = signedPreKeyStore;
    }

    public List listAllAccounts() {
        RealmResults<Account> accounts = realm.where(Account.class).findAll();
        return new ArrayList<>(accounts);
    }

    public Account createAccount(String name) {

        try {
            Account sameName = realm.where(Account.class).equalTo("name", name).findFirst();
            if (sameName != null)
                return null;

            IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
            int registrationId = KeyHelper.generateRegistrationId(false);

            Account account = new Account();
            account.setId(generateAccountId());
            account.setKeyPair(identityKeyPair.serialize());
            account.setName(name);
            byte[][] typeAndKey = ByteUtil.split(identityKeyPair.getPublicKey().serialize(), 1, 32);
            account.setKeyBase64(Base64.encodeToString(typeAndKey[1], Base64.URL_SAFE | Base64.NO_WRAP));
            account.setRegistrationId(registrationId);


            List<PreKeyRecord> preKeyRecords = KeyHelper.generatePreKeys(0, 50);
            RealmList<OneTimeKey> oneTimeKeys = new RealmList<>();

            for (PreKeyRecord record : preKeyRecords) {
                preKeyStore.storePreKey(record.getId(), record);
                realm.beginTransaction();
                OneTimeKey temporaryKey = realm.where(OneTimeKey.class).equalTo("oneTimeKeyId", record.getId()).findFirst();
                oneTimeKeys.add(temporaryKey);
                realm.commitTransaction();
            }

            SignedPreKeyRecord signedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKeyPair, 0);
            signedPreKeyStore.storeSignedPreKey(signedPreKeyRecord.getId(), signedPreKeyRecord);

            SignedKey signedKey = realm.where(SignedKey.class).equalTo("signedKeyId", signedPreKeyRecord.getId()).findFirst();

            account.setOneTimeKeys(oneTimeKeys);

            realm.beginTransaction();
            account = realm.copyToRealmOrUpdate(account);
            signedKey = realm.copyToRealmOrUpdate(signedKey);
            signedKey.setAccount(account);
            account.setSignedKey(signedKey);

            realm.commitTransaction();

            return account;
        } catch (InvalidKeyException e) {
            Log.getStackTraceString(e);
            return null;
        }

    }

    public String getActiveAccountName() {
        Account activeAccount = realm.where(Account.class).equalTo("active", true).findFirst();
        return activeAccount == null ? null : activeAccount.getName();
    }

    public Account getActiveAccount() {
        Realm realm = Realm.getDefaultInstance();
        Account account = realm.where(Account.class).equalTo("active", true).findFirst();
        realm.close();
        return account;
    }

    public boolean chooseAccount(String name) {

        Account anyAccount = realm.where(Account.class).equalTo("name", name).findFirst();

        if (anyAccount == null)
            return false;

        realm.beginTransaction();
        RealmResults<Account> activeAccounts = realm.where(Account.class).equalTo("active", true).findAll();

        for (Account account : activeAccounts) {
            account.setActive(false);
            realm.copyToRealmOrUpdate(account);
        }

        Account chosenAccount = realm.where(Account.class).equalTo("name", name).findFirst();
        chosenAccount.setActive(true);
        realm.copyToRealmOrUpdate(chosenAccount);
        realm.commitTransaction();

        return true;
    }

    public boolean deleteAccount(String name) {

        Account sameName = realm.where(Account.class).equalTo("name", name).findFirst();
        if (sameName == null)
            return false;

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

        Log.v(TAG, "DELETING ACCOUNT " + name);
        Log.v(TAG, realm.where(Account.class).findAll().toString());

        return true;
    }

    /***
     * Logs in as active account
     *
     * @return success
     */
    public void signUp(final Account account) {

        Call<ResponseBody> call = userService.signUp(account.getKeyBase64());
        Log.v(TAG, "Sign up call to : " + call.request().url());
        Log.v(TAG, "Sign up method : " + call.request().method());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.v(TAG, "Sign up response: " + response.message());
                Log.v(TAG, "Sign up response code: " + response.code());

                if (response.isSuccessful()) {
                    realm.beginTransaction();
                    account.setRegistered(true);
                    realm.insertOrUpdate(account);
                    realm.commitTransaction();
                    Log.v(TAG, "Account " + account.getName() + " registered");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v(TAG, "Account " + account.getName() + " failed to sign up");
                Log.v("HAI", t.getMessage() + " " + t.getCause() + " ");
                Log.getStackTraceString(t);
            }
        });

    }

    private String requestCode() {
        Realm realm2 = Realm.getDefaultInstance();

        try {

            Account active = realm.where(Account.class).equalTo("active", true).equalTo("registered", true).findFirst();
            if (active == null)
                return null;

            Call<String> requestCodeCall = userService.requestCode(active.getKeyBase64());
            Response<String> codeCallResponse;

            codeCallResponse = requestCodeCall.execute();

            Log.v(TAG, "request code url " + requestCodeCall.request().url());

            if (!codeCallResponse.isSuccessful()) {
                Log.v(TAG, "Failed to fetch code");
                return null;
            }

            Log.v(TAG, "Fetched code " + codeCallResponse.body());
            Log.v(TAG, "Fetching status " + codeCallResponse.code());

            realm2.close();
            return codeCallResponse.body();

        } catch (IOException e) {
            Log.getStackTraceString(e);
            realm2.close();
            return null;
        }
        finally {
            realm2.close();
        }
    }

    public String logIn() {
        Realm realm = Realm.getDefaultInstance();

        try {

            Account active = realm.where(Account.class).equalTo("active", true).equalTo("registered", true).findFirst();
            if (active == null)
                return null;

            IdentityKeyPair keyPair = new IdentityKeyPair(active.getKeyPair());
            byte[] signature = curve25519.calculateSignature(keyPair.getPrivateKey().serialize(), requestCode().getBytes());

            LoginDetails loginDetails = new LoginDetails(active.getKeyBase64(), Base64.encodeToString(signature, Base64.URL_SAFE | Base64.NO_WRAP));

            Call<ResponseBody> loginCall = userService.login(loginDetails);
            Response<ResponseBody> loginCallResponse = loginCall.execute();

            Log.v(TAG, "login call code:" + loginCallResponse.code());

            String cookie = loginCallResponse.headers().get("Set-Cookie").split(";")[0];

            realm.beginTransaction();
            Account active2 = realm.where(Account.class).equalTo("active", true).findFirst();
            Log.v(TAG, "Cookie " + cookie);
            active2.setCookie(cookie);
            realm.copyToRealmOrUpdate(active2);
            realm.commitTransaction();


            if (!loginCallResponse.isSuccessful()) {
                Log.v(TAG, "Failed to login");
                return null;
            }

            Log.v(TAG, "Login successful");
            realm.close();
            return cookie;

        } catch (IOException | InvalidKeyException e) {
            realm.close();
            Log.getStackTraceString(e);
            return null;
        }
        finally {
            realm.close();
        }
    }

    public String getOneTimeKey(String keyBase64) throws InvalidKeyException, IOException {

        Call<OneTimeKeyTO> getOneTimeKeyCall = keyService.getOneTimeKey(keyBase64);
        Log.v(TAG, "request code url " + getOneTimeKeyCall.request().url());
        Response<OneTimeKeyTO> getOneTimeKeyCallResponse = getOneTimeKeyCall.execute();

        if (!getOneTimeKeyCallResponse.isSuccessful()) {
            Log.v(TAG, "Failed to fetch code");
            return null;
        }

        Log.v(TAG, "Fetched code " + getOneTimeKeyCallResponse.body());
        Log.v(TAG, "Fetching status " + getOneTimeKeyCallResponse.code());

        return "Success";
    }

    public String getSignedKey(String keyBase64) throws InvalidKeyException, IOException {

        Call<SignedKeyTO> getSignedKeyCall = keyService.getSignedKey(keyBase64);
        Log.v(TAG, "request code url " + getSignedKeyCall.request().url());
        Response<SignedKeyTO> getSignedKeyCallResponse = getSignedKeyCall.execute();

        if (!getSignedKeyCallResponse.isSuccessful()) {
            Log.v(TAG, "Failed to fetch code");
            return null;
        }

        Log.v(TAG, "Fetched code " + getSignedKeyCallResponse.body());
        Log.v(TAG, "Fetching status " + getSignedKeyCallResponse.code());

        return "Success";
    }

    public String postOneTimeKeys() throws IOException {
        Realm realm2 = Realm.getDefaultInstance();
        Account activeAcc = realm.where(Account.class).equalTo("active", true).equalTo("registered", true).findFirst();
        Log.d("HAI/ACTIVE ACCOUNT", activeAcc.getName());
        realm2.close();

        if (activeAcc.getOneTimeKeys() == null || activeAcc.getOneTimeKeys().isEmpty()) {
            Log.d(TAG, "There are no one time keys to be fetched");
            return null;
        }

        List<OneTimeKey> otkeys = new ArrayList<>(activeAcc.getOneTimeKeys());
        List<OneTimeKeyTO> otkeysTO = new ArrayList<>();
        for (OneTimeKey otk : otkeys) {
            Log.d("OTK", new String(otk.getSerializedKey()));
            otkeysTO.add(new OneTimeKeyTO(otk));
        }

        Call<ResponseBody> postOneTimeKeysCall = keyService.postOneTimeKeys(otkeysTO, activeAcc.getCookie());
        Response<ResponseBody> postOneTimeKeysResponse = postOneTimeKeysCall.execute();


        Log.v(TAG, "post one time keys call code:" + postOneTimeKeysResponse.code());
        Log.v(TAG, "post one time keys call keys:" + Arrays.toString(otkeysTO.toArray()));
        Log.v(TAG, "post one time keys call url:" + postOneTimeKeysCall.request().url());

        for (String name : postOneTimeKeysResponse.headers().names()) {
            Log.v(TAG, name + " : " + postOneTimeKeysResponse.headers().get(name));
        }

        if (!postOneTimeKeysResponse.isSuccessful()) {
            Log.v(TAG, "Failed to post one time keys");
            return null;
        }

        Log.v(TAG, "PostOneTimeKeys successful");
        return "Success";
    }

    public String postSignedKey() throws IOException {
        Realm realm = Realm.getDefaultInstance();
        Account activeAcc = realm.where(Account.class).equalTo("active", true).equalTo("registered", true).findFirst();
        Account active = realm.copyFromRealm(activeAcc);
        Log.d("HAI/ACTIVE ACCOUNT", activeAcc.getName());

        realm.close();

        if (active.getSignedKey() == null) {
            Log.d(TAG, "There are no signed keys to be fetched");
            return null;
        }

        Call<ResponseBody> postSignedKeysCall = keyService.postSignedKeys(new SignedKeyTO(active.getSignedKey()), active.getCookie());
        Response<ResponseBody> postSignedKeysResponse = postSignedKeysCall.execute();

        Log.v(TAG, "post signed keys call code:" + postSignedKeysResponse.code());
        Log.v(TAG, "post signed keys call url:" + postSignedKeysCall.request().url());

        for (String name : postSignedKeysResponse.headers().names()) {
            Log.v(TAG, name + " : " + postSignedKeysResponse.headers().get(name));
        }

        if (!postSignedKeysResponse.isSuccessful()) {
            Log.v(TAG, "Failed to post signed keys");
            return null;
        }

        Log.v(TAG, "PostSignedKeys successful");
        return "Success";
    }

    /**
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
        } catch (ArrayIndexOutOfBoundsException e) {
            id = 0;
            Log.getStackTraceString(e);
        }
        return id;
    }

    private long generateOtkId() {
        Realm realm = Realm.getDefaultInstance();
        long id;
        try {
            if (realm.where(OneTimeKey.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(OneTimeKey.class).max("id").longValue() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            id = 0;
            Log.getStackTraceString(e);
        }
        realm.close();
        return id;
    }

}
