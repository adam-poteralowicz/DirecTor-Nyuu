package com.apap.director.client.domain.model;

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
public class OneTimeKeyModel {

    private long id;
    private int oneTimeKeyId;
    private byte[] serializedKey;

}
