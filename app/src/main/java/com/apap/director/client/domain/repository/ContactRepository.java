package com.apap.director.client.domain.repository;

import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.domain.model.ContactModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public interface ContactRepository {

    Observable<List<ContactModel>> getContactList();

    Observable<ContactModel> getContact(String ownerId, String name);

    Observable<Long> findNextId();

    Observable<OneTimeKeyTO> getOneTimeKey(ContactModel contactModel);

    Observable<SignedKeyTO> getSignedKey(ContactModel contactModel);

    Observable<ContactModel> updateContact(ContactModel contactModel);
}
