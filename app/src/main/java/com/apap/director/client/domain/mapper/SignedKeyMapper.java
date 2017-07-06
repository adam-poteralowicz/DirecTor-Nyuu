package com.apap.director.client.domain.mapper;

import com.apap.director.client.domain.mapper.base.KeyMapper;
import com.apap.director.client.domain.model.SignedKeyModel;

import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.io.IOException;

/**
 * Created by Alicja Michniewicz
 */

public class SignedKeyMapper extends KeyMapper<SignedKeyModel, SignedPreKeyRecord> {

    @Override
    public SignedKeyModel mapRecordToModel(SignedPreKeyRecord record, long dbId) {
        SignedKeyModel signedKeyModel = new SignedKeyModel();
        signedKeyModel.setId(dbId);
        signedKeyModel.setSignedKeyId(record.getId());
        signedKeyModel.setSerializedKey(record.serialize());

        return signedKeyModel;
    }

    @Override
    public SignedPreKeyRecord mapModelToRecord(SignedKeyModel model) throws IOException {
        return new SignedPreKeyRecord(model.getSerializedKey());
    }
}
