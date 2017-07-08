package com.apap.director.client.data.net.mapper;

import com.apap.director.client.data.net.mapper.base.BaseTOMapper;
import com.apap.director.client.data.net.model.SignedKey;
import com.apap.director.client.data.net.to.SignedKeyTO;

/**
 * Created by Adam Poterałowicz
 */

public class SignedKeyTOMapper extends BaseTOMapper<SignedKey, SignedKeyTO> {

    @Override
    public SignedKeyTO mapToTO(SignedKey model) {
        if (model == null)
            return null;

        SignedKeyTO signedKeyTO = new SignedKeyTO();
        signedKeyTO.setSignedKeyId(model.getSignedKeyId());
        signedKeyTO.setKeyBase64(model.getKeyBase64());
        signedKeyTO.setSignatureBase64(model.getSignatureBase64());

        return signedKeyTO;
    }

    @Override
    public SignedKey mapToModel(SignedKeyTO to) {
        if (to == null)
            return null;

        SignedKey signedKey = new SignedKey();
        signedKey.setSignedKeyId(to.getSignedKeyId());
        signedKey.setKeyBase64(to.getKeyBase64());
        signedKey.setSignatureBase64(to.getSignatureBase64());

        return signedKey;
    }
}
