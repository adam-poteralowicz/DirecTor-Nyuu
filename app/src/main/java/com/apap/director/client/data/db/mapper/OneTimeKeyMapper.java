package com.apap.director.client.data.db.mapper;

import com.apap.director.client.data.db.entity.OneTimeKeyEntity;
import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.domain.model.OneTimeKeyModel;

/**
 * Created by Alicja Michniewicz
 */

public class OneTimeKeyMapper extends BaseMapper<OneTimeKeyModel, OneTimeKeyEntity> {

    @Override
    public OneTimeKeyEntity mapToEntity(OneTimeKeyModel model) {
        if(model == null)
            return null;

        OneTimeKeyEntity entity = new OneTimeKeyEntity();

        entity.setId(model.getId());
        entity.setOneTimeKeyId(model.getOneTimeKeyId());
        entity.setSerializedKey(model.getSerializedKey());

        return entity;
    }

    @Override
    public OneTimeKeyModel mapToModel(OneTimeKeyEntity entity) {
        if(entity == null)
            return null;

        OneTimeKeyModel model = new OneTimeKeyModel();

        model.setId(entity.getId());
        model.setSerializedKey(entity.getSerializedKey());
        model.setOneTimeKeyId(entity.getOneTimeKeyId());

        return model;
    }
}
