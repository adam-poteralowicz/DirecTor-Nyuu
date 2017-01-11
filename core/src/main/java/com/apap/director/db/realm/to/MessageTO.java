package com.apap.director.db.realm.to;


import org.whispersystems.libsignal.protocol.CiphertextMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
@Getter
@Setter
public class MessageTO {

    public String from;
    public String message;
    public int type;

}
