package com.apap.director.client.domain.interactor.contact;

import android.support.v4.util.Pair;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class CreateContactInteractor extends BaseInteractor<ContactModel, Pair<String, String>> {

    @Override
    protected Observable<ContactModel> buildObservable(Pair<String, String> stringStringPair) {
        return null;
    }
}
