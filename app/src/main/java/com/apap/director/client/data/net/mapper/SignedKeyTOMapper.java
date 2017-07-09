package com.apap.director.client.data.net.mapper;

import android.util.Log;

import com.apap.director.client.data.net.mapper.base.BaseTOMapper;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.domain.model.SignedKeyModel;

import java.io.UnsupportedEncodingException;

/**
 * Created by Adam Poterałowicz
 */

public class SignedKeyTOMapper extends BaseTOMapper<SignedKeyModel, SignedKeyTO> {

    @Override
    public SignedKeyTO mapToTO(SignedKeyModel model) {
        SignedKeyTO signedKeyTO = new SignedKeyTO();

        try {
            if (model == null)
                return null;

            signedKeyTO.setSignedKeyId(model.getSignedKeyId());
            signedKeyTO.setKeyBase64(new String(model.getSerializedKey(), "UTF-8"));
            //signedKeyTO.setSignatureBase64(model.getSignatureBase64());
        } catch (UnsupportedEncodingException e) {
            Log.getStackTraceString(e);
        }

        return signedKeyTO;
    }

    @Override
    public SignedKeyModel mapToModel(SignedKeyTO to) {
        SignedKeyModel signedKey = new SignedKeyModel();

        try {
            if (to == null)
                return null;

            signedKey.setSignedKeyId(to.getSignedKeyId());
            signedKey.setSerializedKey(to.getKeyBase64().getBytes("UTF-8"));
            //signedKey.setSignatureBase64(to.getSignatureBase64());
        } catch (UnsupportedEncodingException e) {
            Log.getStackTraceString(e);
        }

        return signedKey;
    }
}
