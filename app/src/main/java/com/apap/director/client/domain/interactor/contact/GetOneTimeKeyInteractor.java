package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.ContactRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class GetOneTimeKeyInteractor extends BaseInteractor<OneTimeKeyTO, ContactModel> {

    private ContactRepository contactRepository;

    @Inject
    public GetOneTimeKeyInteractor(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    protected Observable<OneTimeKeyTO> buildObservable(ContactModel contactModel) {
        return contactRepository.getOneTimeKey(contactModel);
    }
}
