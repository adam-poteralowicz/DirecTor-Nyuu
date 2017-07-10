package com.apap.director.client.data.net.rest.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDetails {

    private String keyBase64;
    private String signatureBase64;

    public LoginDetails() {

    }

    public LoginDetails(String keyBase64, String signatureBase64) {
        this.keyBase64 = keyBase64;
        this.signatureBase64 = signatureBase64;
    }
}
