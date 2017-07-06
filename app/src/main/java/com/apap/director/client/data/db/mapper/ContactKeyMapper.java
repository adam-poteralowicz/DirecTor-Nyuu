package com.apap.director.client.data.db.mapper;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ContactKeyEntity;
import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.domain.model.ContactKeyModel;
import com.apap.director.client.domain.model.ContactModel;

/**
 * Created by Alicja Michniewicz
 */

public class ContactKeyMapper extends BaseMapper<ContactKeyModel, ContactKeyEntity> {

    @Override
    public ContactKeyEntity mapToEntity(ContactKeyModel model) {

        if(model == null) return null;

        ContactKeyEntity contactKeyEntity = new ContactKeyEntity();

        contactKeyEntity.setId(model.getId());
        contactKeyEntity.setKeyBase64(model.getKeyBase64());
        contactKeyEntity.setDeviceId(model.getDeviceId());
        contactKeyEntity.setSerialized(model.getSerialized());

        return contactKeyEntity;
    }

    @Override
    public ContactKeyModel mapToModel(ContactKeyEntity entity) {

        if(entity == null) return null;

        ContactKeyModel contactKeyModel = new ContactKeyModel();

        contactKeyModel.setId(entity.getId());
        contactKeyModel.setKeyBase64(entity.getKeyBase64());
        contactKeyModel.setDeviceId(entity.getDeviceId());
        contactKeyModel.setSerialized(entity.getSerialized());

        return contactKeyModel;

    }
}
