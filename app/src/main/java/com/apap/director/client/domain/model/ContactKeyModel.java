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
public class ContactKeyModel {

    private long id;
    private byte[] serialized;
    private int deviceId;
    private String keyBase64;
    
}
