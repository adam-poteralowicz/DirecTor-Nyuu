package com.apap.director.client.domain.util;

import android.util.Base64;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.util.ByteUtil;

/**
 * Created by Alicja Michniewicz
 */

public class Base64Util {

    private static final int KEY_LENGTH = 32;
    private static final int TYPE_LENGTH = 1;

    public static String convertToBase64(IdentityKey key) {
        byte[][] typeAndKey = ByteUtil.split(key.serialize(), TYPE_LENGTH, KEY_LENGTH);
        return Base64.encodeToString(typeAndKey[1], Base64.URL_SAFE | Base64.NO_WRAP);
    }

    public static String convertToBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);
    }
}
