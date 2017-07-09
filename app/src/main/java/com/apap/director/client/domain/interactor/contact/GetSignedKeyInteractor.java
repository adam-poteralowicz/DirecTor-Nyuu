package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.ContactRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-09.
 */

public class GetSignedKeyInteractor extends BaseInteractor<SignedKeyTO, ContactModel> {

    private ContactRepository contactRepository;

    @Inject
    public GetSignedKeyInteractor(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    protected Observable<SignedKeyTO> buildObservable(ContactModel contactModel) {
        return contactRepository.getSignedKey(contactModel);
    }
}
