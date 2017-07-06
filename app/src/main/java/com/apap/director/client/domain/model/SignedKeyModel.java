package com.apap.director.client.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Alicja Michniewicz
 */

@Getter
@Setter
@NoArgsConstructor
public class SignedKeyModel {

    private long id;
    private int signedKeyId;
    private byte[] serializedKey;

}
