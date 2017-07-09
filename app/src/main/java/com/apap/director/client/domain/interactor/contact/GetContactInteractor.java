package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.ContactRepository;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class GetContactInteractor extends BaseInteractor<ContactModel, String> {

    AccountRepository accountRepository;
    ContactRepository contactRepository;


    @Override
    protected Observable<ContactModel> buildObservable(String name) {
        return null;
    }
}
