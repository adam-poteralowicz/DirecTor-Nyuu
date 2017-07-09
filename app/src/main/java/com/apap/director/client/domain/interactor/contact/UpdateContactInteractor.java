package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.ContactRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class UpdateContactInteractor extends BaseInteractor<ContactModel, ContactModel> {

    private ContactRepository contactRepository;

    @Inject
    public UpdateContactInteractor(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    protected Observable<ContactModel> buildObservable(ContactModel contactModel) {
        return contactRepository.updateContact(contactModel);
    }
}
