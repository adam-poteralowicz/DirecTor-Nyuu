package com.apap.director.im;


import android.accounts.Account;

import com.apap.director.db.manager.DatabaseManager;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

import javax.inject.Inject;

import static org.whispersystems.libsignal.util.KeyHelper.generateIdentityKeyPair;

public class AccountManager {


    private DatabaseManager manager;

    @Inject
    public AccountManager(DatabaseManager manager){
        this.manager = manager;
    }

    public void createAccount(String name){

        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        int registrationId  = KeyHelper.generateRegistrationId(false);



    }




}
