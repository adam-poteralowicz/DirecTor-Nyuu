package com.apap.director.client.presentation.ui.contact.presenter;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.AddContactContract;

import javax.inject.Inject;

/**
 * Created by Adam on 2017-07-03.
 */

public class AddContactPresenter implements BasePresenter, AddContactContract.Presenter {

    private AddContactContract.View view;

    @Inject
    AddContactPresenter(AddContactContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {

    }
}
