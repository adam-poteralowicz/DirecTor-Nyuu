package com.apap.director.client.domain.model;

import com.apap.director.client.data.db.entity.OneTimeKeyEntity;
import com.apap.director.client.data.db.entity.SessionEntity;
import com.apap.director.client.data.db.entity.SignedKeyEntity;

import java.util.List;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Alicja Michniewicz
 */

@Getter
@Setter
@NoArgsConstructor
public class AccountModel {

    private String keyBase64;
    private String name;
    private byte[] keyPair;
    private int registrationId;
    private String masterPassword;
    private List<OneTimeKeyModel> oneTimeKeys;
    private SignedKeyModel signedKey;
    private boolean registered;
    private boolean active;
    private String cookie;
}
