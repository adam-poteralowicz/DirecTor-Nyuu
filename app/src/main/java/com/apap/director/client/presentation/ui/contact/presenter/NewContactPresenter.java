package com.apap.director.client.presentation.ui.contact.presenter;

import android.support.v4.util.Pair;

import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.domain.interactor.contact.CreateContactInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.NewContactContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Adam Poterałowicz
 */

public class NewContactPresenter implements BasePresenter, NewContactContract.Presenter {

    @Inject
    ContactManager contactManager;

    private NewContactContract.View view;
    private CreateContactInteractor createContactInteractor;

    private CompositeDisposable subscriptions;

    @Inject
    NewContactPresenter(NewContactContract.View view, CreateContactInteractor createContactInteractor) {
        this.view = view;
        this.createContactInteractor = createContactInteractor;

        subscriptions = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void addContact(String name, String publicKey) {
        if (name.length() == 0) {
            view.showSnackbar("Type a valid name");
        } else {
            Pair<String, String> nameAndKey = Pair.create(name, publicKey);
            subscriptions.add(createContactInteractor.execute(nameAndKey)
                    .subscribe(contactModel -> {
                                String message = "Contact " + name + " created.";
                                view.handleSuccess(message);
                                view.showSnackbar(message);
                            },
                            throwable -> view.handleException(throwable)));
        }
    }
}
