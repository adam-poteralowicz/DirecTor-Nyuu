package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.ContactRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class GetContactListInteractor extends BaseInteractor<List<ContactModel>, Void> {

    private ContactRepository contactRepository;

    @Inject
    public GetContactListInteractor(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Observable<List<ContactModel>> buildObservable(Void unused) {
        return contactRepository.getContactList();
    }
}
