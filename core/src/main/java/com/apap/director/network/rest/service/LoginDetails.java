package com.apap.director.network.rest.service;

public class LoginDetails{

    private String keyBase64;
    private String signatureBase64;

    public LoginDetails(){

    }

    public LoginDetails(String keyBase64, String signatureBase64) {
        this.keyBase64 = keyBase64;
        this.signatureBase64 = signatureBase64;
    }

    public String getKeyBase64() {
        return keyBase64;
    }

    public void setKeyBase64(String keyBase64) {
        this.keyBase64 = keyBase64;
    }

    public String getSignatureBase64() {
        return signatureBase64;
    }

    public void setSignatureBase64(String signatureBase64) {
        this.signatureBase64 = signatureBase64;
    }
}
