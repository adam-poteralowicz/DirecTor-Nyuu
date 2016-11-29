package com.apap.director.im.signal;


import org.greenrobot.greendao.annotation.Entity;

@Entity
public class SignedPreKeyRecord {

    private int id;
    private byte[] serialized;

}
