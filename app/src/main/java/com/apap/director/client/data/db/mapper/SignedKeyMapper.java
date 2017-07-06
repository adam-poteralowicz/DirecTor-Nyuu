package com.apap.director.client.data.db.mapper;

import com.apap.director.client.data.db.entity.SignedKeyEntity;
import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.domain.model.SignedKeyModel;

/**
 * Created by Alicja Michniewicz
 */

public class SignedKeyMapper extends BaseMapper<SignedKeyModel, SignedKeyEntity> {

    @Override
    public SignedKeyEntity mapToEntity(SignedKeyModel model) {
        if(model == null) return null;

        SignedKeyEntity entity = new SignedKeyEntity();
        entity.setId(model.getId());
        entity.setSerializedKey(model.getSerializedKey());
        entity.setSignedKeyId(model.getSignedKeyId());

        return entity;
    }

    @Override
    public SignedKeyModel mapToModel(SignedKeyEntity entity) {
        if(entity == null) return null;

        SignedKeyModel model = new SignedKeyModel();
        model.setId(entity.getId());
        model.setSerializedKey(entity.getSerializedKey());
        model.setSignedKeyId(entity.getSignedKeyId());

        return model;
    }
}
