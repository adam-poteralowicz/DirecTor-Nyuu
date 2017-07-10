package com.apap.director.client.presentation.ui.message.presenter;

import com.apap.director.client.data.db.mapper.MessageMapper;
import com.apap.director.client.data.db.service.DbMessageService;
import com.apap.director.client.domain.interactor.message.DeleteMessageInteractor;
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
    private DeleteMessageInteractor deleteMessageInteractor;

    private CompositeDisposable subscriptions;
    private DbMessageService dbMessageService;
    private MessageMapper messageMapper;

    @Inject
    NewMsgPresenter(NewMsgContract.View view, GetMessageListInteractor getMessageListInteractor, DeleteMessageInteractor deleteMessageInteractor, DbMessageService dbMessageService) {
        this.view = view;
        this.getMessageListInteractor = getMessageListInteractor;
        this.deleteMessageInteractor = deleteMessageInteractor;
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

    @Override
    public void deleteMessage(Long messageId) {
        subscriptions.add(deleteMessageInteractor.execute(messageMapper.mapToModel(dbMessageService.getMessageByMessageId(messageId)))
                .subscribe(aBoolean -> view.handleSuccess("Message with id " + messageId + " deleted"),
                        throwable -> view.handleException(throwable)));
    }
}
