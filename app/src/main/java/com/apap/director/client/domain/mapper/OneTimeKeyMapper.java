package com.apap.director.client.domain.mapper;

import com.apap.director.client.domain.mapper.base.KeyMapper;
import com.apap.director.client.domain.model.OneTimeKeyModel;

import org.whispersystems.libsignal.state.PreKeyRecord;

import java.io.IOException;

/**
 * Created by Alicja Michniewicz
 */

public class OneTimeKeyMapper extends KeyMapper<OneTimeKeyModel, PreKeyRecord> {

    @Override
    public OneTimeKeyModel mapRecordToModel(PreKeyRecord record, long dbId) {
        OneTimeKeyModel model = new OneTimeKeyModel();
        model.setId(dbId);
        model.setSerializedKey(record.serialize());
        model.setOneTimeKeyId(record.getId());

        return model;
    }

    @Override
    public PreKeyRecord mapModelToRecord(OneTimeKeyModel model) throws IOException {
        return new PreKeyRecord(model.getSerializedKey());
    }
}
