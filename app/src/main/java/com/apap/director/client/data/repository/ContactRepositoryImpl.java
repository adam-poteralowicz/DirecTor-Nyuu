package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.service.DbContactService;
import com.apap.director.client.domain.repository.ContactRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-03.
 */

public class ContactRepositoryImpl implements ContactRepository {

    private DbContactService dbContactService;

    @Inject
    public ContactRepositoryImpl(DbContactService dbContactService) {
        this.dbContactService = dbContactService;
    }

    @Override
    public Observable<List<ContactEntity>> getContactList() {
        return Observable.just(dbContactService.getContactList());
    }
}
