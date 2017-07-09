package com.apap.director.client.data.db.mapper;

import com.apap.director.client.data.db.entity.SessionEntity;
import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.domain.model.SessionModel;

import javax.inject.Inject;

/**
 * Created by Adam Poterałowicz
 */

public class SessionMapper extends BaseMapper<SessionModel, SessionEntity> {

    AccountMapper accountMapper;

    @Inject
    public SessionMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public SessionEntity mapToEntity(SessionModel model) {
        if (model == null)
            return null;

        SessionEntity entity = new SessionEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setSerializedKey(model.getSerializedKey());
        entity.setDeviceId(model.getDeviceId());
        entity.setOwner();
        return entity;
    }

    @Override
    public SessionModel mapToModel(SessionEntity entity) {
        if (entity == null)
            return null;

        SessionModel model = new SessionModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setSerializedKey(entity.getSerializedKey());
        model.setDeviceId(entity.getDeviceId());

        return model;
    }
}

