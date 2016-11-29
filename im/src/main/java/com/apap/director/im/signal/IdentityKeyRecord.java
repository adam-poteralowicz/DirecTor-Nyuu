package com.apap.director.im.signal;


import org.greenrobot.greendao.annotation.Entity;

@Entity
public class IdentityKeyRecord {

    private int identityKeyId;
    private String name;
    private byte[] serialized;


}
