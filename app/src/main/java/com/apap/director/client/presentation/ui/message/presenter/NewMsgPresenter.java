package com.apap.director.client.presentation.ui.message.presenter;

import com.apap.director.client.data.db.service.DbMessageService;
import com.apap.director.client.domain.interactor.message.GetMessageListInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.message.contract.NewMsgContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Adam Poterałowicz
 */

public class NewMsgPresenter implements BasePresenter, NewMsgContract.Presenter {

    private NewMsgContract.View view;
    private GetMessageListInteractor getMessageListInteractor;

    private CompositeDisposable subscriptions;
    private DbMessageService dbMessageService;

    @Inject
    NewMsgPresenter(NewMsgContract.View view, GetMessageListInteractor getMessageListInteractor, DbMessageService dbMessageService) {
        this.view = view;
        this.getMessageListInteractor = getMessageListInteractor;
        this.dbMessageService = dbMessageService;

        subscriptions = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void getMessagesByContact(Long contactIdFromIntent) {
        getMessageListInteractor.execute(contactIdFromIntent)
                .subscribe(data -> view.refreshMessageList(dbMessageService.getMessagesByContact(contactIdFromIntent)),
                        throwable -> view.handleException(throwable));
    }
}
