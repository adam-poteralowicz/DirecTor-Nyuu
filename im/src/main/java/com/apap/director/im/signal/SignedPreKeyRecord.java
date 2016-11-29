package com.apap.director.im.signal;


import org.greenrobot.greendao.annotation.Entity;

@Entity
public class SignedPreKeyRecord {

    private int signedPreKeyId;
    private byte[] serialized;

}
