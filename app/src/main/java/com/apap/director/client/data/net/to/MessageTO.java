package com.apap.director.client.data.net.to;


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
