package com.apap.director.client.data.db.mapper;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.domain.model.ContactModel;

import javax.inject.Inject;

/**
 * Created by Alicja Michniewicz
 */

public class ContactMapper extends BaseMapper<ContactModel, ContactEntity> {

    private ContactKeyMapper contactKeyMapper;
    private AccountMapper accountMapper;

    @Inject
    public ContactMapper(ContactKeyMapper contactKeyMapper, AccountMapper accountMapper) {
        this.contactKeyMapper = contactKeyMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    public ContactEntity mapToEntity(ContactModel model) {
        if(model == null)
            return null;

        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setName(model.getName());
        contactEntity.setId(model.getId());
        contactEntity.setContactKeys(mapToRealmList(contactKeyMapper, model.getContactKeys()));
        contactEntity.setImage(model.getImage());
        contactEntity.setOwner(accountMapper.mapToEntity(model.getOwner()));

        return contactEntity;
    }

    @Override
    public ContactModel mapToModel(ContactEntity entity) {
        if(entity == null)
            return null;

        ContactModel contactModel = new ContactModel();
        contactModel.setName(entity.getName());
        contactModel.setId(entity.getId());
        contactModel.setContactKeys(mapToList(contactKeyMapper, entity.getContactKeys()));
        contactModel.setImage(entity.getImage());
        contactModel.setOwner(accountMapper.mapToModel(entity.getOwner()));

        return contactModel;
    }
}
