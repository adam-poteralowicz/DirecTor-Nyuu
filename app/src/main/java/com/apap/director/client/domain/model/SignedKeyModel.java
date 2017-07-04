package com.apap.director.client.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Alicja Michniewicz
 */

@Getter
@Setter
public class SignedKeyModel {

    private long id;
    private int signedKeyId;
    private byte[] serializedKey;

}
