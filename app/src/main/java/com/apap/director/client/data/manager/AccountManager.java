package com.apap.director.client.data.manager;

import android.util.Base64;
import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactKeyEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.entity.OneTimeKeyEntity;
import com.apap.director.client.data.db.entity.SessionEntity;
import com.apap.director.client.data.db.entity.SignedKeyEntity;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.LoginDetails;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.data.store.SignedPreKeyStoreImpl;
import com.apap.director.client.data.db.entity.MessageEntity;


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
    private RestAccountService restAccountService;
    private Curve25519 curve25519;
    private KeyService keyService;
    private PreKeyStoreImpl preKeyStore;
    private SignedPreKeyStoreImpl signedPreKeyStore;
    private String TAG = this.getClass().getSimpleName();

    public AccountManager(Realm realm, RestAccountService restAccountService, Curve25519 curve25519, KeyService keyService, PreKeyStoreImpl preKeyStore, SignedPreKeyStoreImpl signedPreKeyStore) {
        this.realm = realm;
        this.restAccountService = restAccountService;
        this.curve25519 = curve25519;
        this.keyService = keyService;
        this.preKeyStore = preKeyStore;
        this.signedPreKeyStore = signedPreKeyStore;
    }

    public AccountEntity createAccount(String name) {

        try {
            AccountEntity sameName = realm.where(AccountEntity.class).equalTo("name", name).findFirst();
            if (sameName != null)
                return null;

            IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
            int registrationId = KeyHelper.generateRegistrationId(false);

            AccountEntity account = new AccountEntity();
            account.setId(generateAccountId());
            account.setKeyPair(identityKeyPair.serialize());
            account.setName(name);
            byte[][] typeAndKey = ByteUtil.split(identityKeyPair.getPublicKey().serialize(), 1, 32);
            account.setKeyBase64(Base64.encodeToString(typeAndKey[1], Base64.URL_SAFE | Base64.NO_WRAP));
            account.setRegistrationId(registrationId);


            List<PreKeyRecord> preKeyRecords = KeyHelper.generatePreKeys(0, 50);
            RealmList<OneTimeKeyEntity> oneTimeKeys = new RealmList<>();

            for (PreKeyRecord record : preKeyRecords) {
                preKeyStore.storePreKey(record.getId(), record);
                realm.beginTransaction();
                OneTimeKeyEntity temporaryKey = realm.where(OneTimeKeyEntity.class).equalTo("oneTimeKeyId", record.getId()).findFirst();
                oneTimeKeys.add(temporaryKey);
                realm.commitTransaction();
            }

            SignedPreKeyRecord signedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKeyPair, 0);
            signedPreKeyStore.storeSignedPreKey(signedPreKeyRecord.getId(), signedPreKeyRecord);

            SignedKeyEntity signedKey = realm.where(SignedKeyEntity.class).equalTo("signedKeyId", signedPreKeyRecord.getId()).findFirst();

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
        AccountEntity activeAccount = realm.where(AccountEntity.class).equalTo("active", true).findFirst();
        return activeAccount == null ? null : activeAccount.getName();
    }

    public AccountEntity getActiveAccount() {
        Realm realm = Realm.getDefaultInstance();
        AccountEntity account = realm.where(AccountEntity.class).equalTo("active", true).findFirst();
        realm.close();
        return account;
    }

    public boolean chooseAccount(String name) {

        AccountEntity anyAccount = realm.where(AccountEntity.class).equalTo("name", name).findFirst();

        if (anyAccount == null)
            return false;

        realm.beginTransaction();
        RealmResults<AccountEntity> activeAccounts = realm.where(AccountEntity.class).equalTo("active", true).findAll();

        for (AccountEntity account : activeAccounts) {
            account.setActive(false);
            realm.copyToRealmOrUpdate(account);
        }

        AccountEntity chosenAccount = realm.where(AccountEntity.class).equalTo("name", name).findFirst();
        chosenAccount.setActive(true);
        realm.copyToRealmOrUpdate(chosenAccount);
        realm.commitTransaction();

        return true;
    }

    public boolean deleteAccount(String name) {

        AccountEntity sameName = realm.where(AccountEntity.class).equalTo("name", name).findFirst();
        if (sameName == null)
            return false;

        realm.beginTransaction();
        RealmResults<OneTimeKeyEntity> oneTimeKeys = realm.where(OneTimeKeyEntity.class).equalTo("account.name", name).findAll();
        oneTimeKeys.deleteAllFromRealm();

        RealmResults<MessageEntity> messages = realm.where(MessageEntity.class).equalTo("account.name", name).findAll();
        messages.deleteAllFromRealm();

        RealmResults<SignedKeyEntity> signedKeys = realm.where(SignedKeyEntity.class).equalTo("account.name", name).findAll();
        signedKeys.deleteAllFromRealm();

        RealmResults<SessionEntity> sessions = realm.where(SessionEntity.class).equalTo("account.name", name).findAll();
        sessions.deleteAllFromRealm();

        RealmResults<ConversationEntity> conversations = realm.where(ConversationEntity.class).equalTo("account.name", name).findAll();
        conversations.deleteAllFromRealm();

        RealmResults<ContactKeyEntity> contactKeys = realm.where(ContactKeyEntity.class).equalTo("account.name", name).findAll();
        contactKeys.deleteAllFromRealm();

        realm.where(AccountEntity.class).equalTo("name", name).findFirst().deleteFromRealm();
        realm.commitTransaction();

        Log.v(TAG, "DELETING ACCOUNT " + name);
        Log.v(TAG, realm.where(AccountEntity.class).findAll().toString());

        return true;
    }

    /***
     * Logs in as active account
     *
     * @return success
     */
    public void signUp(final AccountEntity account) {

//        Call<ResponseBody> call = restAccountService.signUp(account.getKeyBase64());
//        Log.v(TAG, "Sign up call to : " + call.request().url());
//        Log.v(TAG, "Sign up method : " + call.request().method());
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                Log.v(TAG, "Sign up response: " + response.message());
//                Log.v(TAG, "Sign up response code: " + response.code());
//
//                if (response.isSuccessful()) {
//                    realm.beginTransaction();
//                    account.setRegistered(true);
//                    realm.insertOrUpdate(account);
//                    realm.commitTransaction();
//                    Log.v(TAG, "AccountEntity " + account.getName() + " registered");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.v(TAG, "AccountEntity " + account.getName() + " failed to sign up");
//                Log.v("HAI", t.getMessage() + " " + t.getCause() + " ");
//                Log.getStackTraceString(t);
//            }
//        });

    }

    private String requestCode() {
//        Realm realm2 = Realm.getDefaultInstance();
//
//        try {
//
//            AccountEntity active = realm.where(AccountEntity.class).equalTo("active", true).equalTo("registered", true).findFirst();
//            if (active == null)
//                return null;

           // Call<String> requestCodeCall = restAccountService.requestCode(active.getKeyBase64());
 //           Response<String> codeCallResponse;

           // codeCallResponse = requestCodeCall.execute();

          //  Log.v(TAG, "request code url " + requestCodeCall.request().url());

          //  if (!codeCallResponse.isSuccessful()) {
          //      Log.v(TAG, "Failed to fetch code");
         //       return null;
         //   }

          //  Log.v(TAG, "Fetched code " + codeCallResponse.body());
         //   Log.v(TAG, "Fetching status " + codeCallResponse.code());

  //          realm2.close();
          //  return codeCallResponse.body();

      //  } catch (IOException e) {
//            Log.getStackTraceString(e);
//            realm2.close();
//            return null;
//        }
//        finally {
//            realm2.close();
//        }

 return null;
    }

    public String logIn() {
        Realm realm = Realm.getDefaultInstance();

        try {

            AccountEntity active = realm.where(AccountEntity.class).equalTo("active", true).equalTo("registered", true).findFirst();
            if (active == null)
                return null;

            IdentityKeyPair keyPair = new IdentityKeyPair(active.getKeyPair());
            byte[] signature = curve25519.calculateSignature(keyPair.getPrivateKey().serialize(), requestCode().getBytes());

            LoginDetails loginDetails = new LoginDetails(active.getKeyBase64(), Base64.encodeToString(signature, Base64.URL_SAFE | Base64.NO_WRAP));

            Call<ResponseBody> loginCall = restAccountService.login(loginDetails);
            Response<ResponseBody> loginCallResponse = loginCall.execute();

            Log.v(TAG, "login call code:" + loginCallResponse.code());

            String cookie = loginCallResponse.headers().get("Set-Cookie").split(";")[0];

            realm.beginTransaction();
            AccountEntity active2 = realm.where(AccountEntity.class).equalTo("active", true).findFirst();
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
        AccountEntity activeAcc = realm.where(AccountEntity.class).equalTo("active", true).equalTo("registered", true).findFirst();
        Log.d("HAI/ACTIVE ACCOUNT", activeAcc.getName());
        realm2.close();

        if (activeAcc.getOneTimeKeys() == null || activeAcc.getOneTimeKeys().isEmpty()) {
            Log.d(TAG, "There are no one time keys to be fetched");
            return null;
        }

        List<OneTimeKeyEntity> otkeys = new ArrayList<>(activeAcc.getOneTimeKeys());
        List<OneTimeKeyTO> otkeysTO = new ArrayList<>();
        for (OneTimeKeyEntity otk : otkeys) {
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
        AccountEntity activeAcc = realm.where(AccountEntity.class).equalTo("active", true).equalTo("registered", true).findFirst();
        AccountEntity active = realm.copyFromRealm(activeAcc);
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
     * @return id for new Realm AccountEntity object
     */
    private long generateAccountId() {
        long id;
        try {
            if (realm.where(AccountEntity.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(AccountEntity.class).max("id").longValue() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            id = 0;
            Log.getStackTraceString(e);
        }
        return id;
    }


}
