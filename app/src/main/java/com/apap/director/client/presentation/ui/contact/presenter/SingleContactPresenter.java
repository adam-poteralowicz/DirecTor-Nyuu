package com.apap.director.client.presentation.ui.contact.presenter;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.SingleContactContract;

import javax.inject.Inject;

/**
 * Created by Adam on 2017-07-03.
 */

public class SingleContactPresenter implements BasePresenter, SingleContactContract.Presenter {

    private SingleContactContract.View view;

    @Inject
    SingleContactPresenter(SingleContactContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {

    }
}
