package com.apap.director.client.domain.util;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;

/**
 * Created by Alicja Michniewicz
 */

public class EncryptionUtil {

    private static Curve25519 curve = Curve25519.getInstance(Curve25519.BEST);

    public static String signMessage(IdentityKeyPair keyPair, String message) {
        return new String(curve.calculateSignature(keyPair.getPrivateKey().serialize(), message.getBytes()));
    }
}
