package com.apap.director.im.signal;


import org.greenrobot.greendao.annotation.Entity;

@Entity
public class IdentityKeyRecord {

    private int id;
    private String name;
    private byte[] serialized;

}
