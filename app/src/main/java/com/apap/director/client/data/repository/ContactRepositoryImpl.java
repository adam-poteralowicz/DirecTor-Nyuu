package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.mapper.ContactMapper;
import com.apap.director.client.data.db.service.DbContactService;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.ContactRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-03.
 */

public class ContactRepositoryImpl implements ContactRepository {

    private DbContactService dbContactService;
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
    public Observable<ContactModel> getContact(String ownerId, String name) {
        return
    }

    @Override
    public Observable<Long> findNextId() {
        return Observable.just(dbContactService.findLastId());
    }
}
