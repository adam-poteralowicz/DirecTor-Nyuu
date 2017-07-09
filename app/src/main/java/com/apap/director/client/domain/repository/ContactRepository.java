package com.apap.director.client.domain.repository;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.domain.model.ContactModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-03.
 */

public interface ContactRepository {

    Observable<List<ContactModel>> getContactList();

    Observable<Long> findNextId();
}
