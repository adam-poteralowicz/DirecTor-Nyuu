package com.apap.director.client.domain.interactor.contact;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.ContactRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class GetContactInteractor extends BaseInteractor<ContactModel, String> {

    AccountRepository accountRepository;
    ContactRepository contactRepository;

    @Inject
    public GetContactInteractor(AccountRepository accountRepository, ContactRepository contactRepository) {
        this.accountRepository = accountRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    protected Observable<ContactModel> buildObservable(String key) {
        return accountRepository.getActiveAccount()
                .flatMap(accountModel -> contactRepository.getContact(accountModel.getKeyBase64(), key));
    }
}
