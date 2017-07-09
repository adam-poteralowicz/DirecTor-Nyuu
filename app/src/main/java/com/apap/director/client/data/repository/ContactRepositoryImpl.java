package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.mapper.ContactMapper;
import com.apap.director.client.data.db.service.DbContactService;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.ContactRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class  ContactRepositoryImpl implements ContactRepository {

    private DbContactService dbContactService;
    private KeyService keyService;
    private ContactMapper contactMapper;

    @Inject
    public ContactRepositoryImpl(DbContactService dbContactService, ContactMapper contactMapper) {
        this.dbContactService = dbContactService;
        this.contactMapper = contactMapper;
    }

    @Override
    public Observable<List<ContactModel>> getContactList() {
        return Observable.just(contactMapper.mapToList(contactMapper, dbContactService.getContactList()));
    }

    @Override
    public Observable<ContactModel> getContact(String ownerId, String key) {
        ContactEntity contactEntity = dbContactService.getContactByKey(ownerId, key);

        if(contactEntity == null) {
            return Observable.empty();
        }
        else {
            return Observable.just(contactMapper.mapToModel(contactEntity));
        }
    }

    @Override
    public Observable<Long> findNextId() {
        return Observable.just(dbContactService.findLastId());
    }

    @Override
    public Observable<OneTimeKeyTO> getOneTimeKey(ContactModel contactModel) {
        String ownerId = contactModel.getContactKey().getKeyBase64();
        return keyService.getOneTimeKey(ownerId);
    }

    @Override
    public Observable<SignedKeyTO> getSignedKey(ContactModel contactModel) {
        String ownerId = contactModel.getContactKey().getKeyBase64();
        return keyService.getSignedKey(ownerId);
    }

    @Override
    public Observable<ContactModel> updateContact(ContactModel contactModel) {
        ContactEntity entity = contactMapper.mapToEntity(contactModel);
        dbContactService.updateContact(entity);
        return Observable.just(contactMapper.mapToModel(entity));
    }
}
