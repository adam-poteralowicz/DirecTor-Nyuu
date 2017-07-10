package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.ContactRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class DeleteContactInteractor extends BaseInteractor<Boolean, ContactModel> {

    private ContactRepository contactRepository;

    @Inject
    public DeleteContactInteractor(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    protected Observable<Boolean> buildObservable(ContactModel contactModel) {
        return contactRepository.deleteContact(contactModel);
    }
}
