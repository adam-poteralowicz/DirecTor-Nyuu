package com.apap.director.client.domain.interactor.contact;

import android.support.v4.util.Pair;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.interactor.message.CreateConversationInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.ContactRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * Created by Alicja Michniewicz
 */

public class CreateContactInteractor extends BaseInteractor<ContactModel, Pair<String, String>> {

    private ContactRepository contactRepository;
    private AccountRepository accountRepository;

    @Inject
    public CreateContactInteractor(ContactRepository contactRepository, AccountRepository accountRepository, CreateConversationInteractor createConversationInteractor) {
        this.contactRepository = contactRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    protected Observable<ContactModel> buildObservable(Pair<String, String> keyAndName) {
        return accountRepository.getActiveAccount()
                .zipWith(contactRepository.findNextId(), (accountModel, aLong) -> preconfigureContactModel(aLong, accountModel, keyAndName));
    }

    private ContactModel preconfigureContactModel(long id, AccountModel owner, Pair<String, String> keyAndName) {
        ContactModel contactModel = new ContactModel();
        contactModel.setName(keyAndName.second);
        contactModel.setOwner(owner);
        contactModel.setId(id);

        return contactModel;
    }
}
