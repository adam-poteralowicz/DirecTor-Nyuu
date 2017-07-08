package com.apap.director.client.domain.model;

import com.apap.director.client.domain.mapper.OneTimeKeyMapper;

import org.whispersystems.libsignal.util.KeyHelper;

import java.util.ArrayList;
import java.util.List;

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
    private List<OneTimeKeyModel> oneTimeKeys;
    private SignedKeyModel signedKey;
    private boolean registered;
    private boolean active;
    private String cookie;



    public void generateOneTimeKeys(int startLocalId, int startPreKeyId, int count) {
        this.oneTimeKeys = new ArrayList<>();

        OneTimeKeyMapper mapper = new OneTimeKeyMapper();

        this.oneTimeKeys = mapper.mapRecordListToModelList(KeyHelper.generatePreKeys(startPreKeyId, count), startLocalId);
    }

}
