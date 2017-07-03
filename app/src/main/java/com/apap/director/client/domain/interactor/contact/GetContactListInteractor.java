package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.repository.ContactRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-03.
 */

public class GetContactListInteractor extends BaseInteractor<List<ContactEntity>, Void> {

    private ContactRepository contactRepository;

    @Inject
    public GetContactListInteractor(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Observable<List<ContactEntity>> buildObservable(Void aVoid) {
        return contactRepository.getContactList();
    }
}
