package com.apap.director.client.data.net.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adam Poterałowicz
 */

@Getter
@Setter
@NoArgsConstructor
public class Message {

    public String from;
    public String message;
    public int type;
}
