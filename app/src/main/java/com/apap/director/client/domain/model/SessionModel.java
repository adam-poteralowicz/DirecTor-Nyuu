package com.apap.director.client.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adam Poterałowicz
 */

@Getter
@Setter
@NoArgsConstructor
public class SessionModel {

    private long id;
    private int deviceId;
    private byte[] serializedKey;
    private String name;
}
