package com.apap.director.db.realm.to;


import org.whispersystems.libsignal.protocol.CiphertextMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(suppressConstructorProperties = true)
@Getter
public class MessageTO {

    public String from;
    public String message;

}
