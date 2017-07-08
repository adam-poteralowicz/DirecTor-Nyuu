package com.apap.director.client.presentation.ui.message.presenter;

import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.domain.interactor.base.Callback;
import com.apap.director.client.domain.interactor.message.GetMessageListInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.message.contract.NewMsgContract;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Adam on 2017-07-04.
 */

public class NewMsgPresenter implements BasePresenter, NewMsgContract.Presenter {

    private NewMsgContract.View view;
    private GetMessageListInteractor getMessageListInteractor;

    @Inject
    NewMsgPresenter(NewMsgContract.View view, GetMessageListInteractor getMessageListInteractor) {
        this.view = view;
        this.getMessageListInteractor = getMessageListInteractor;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void getMessagesByContact(Long contactIdFromIntent) {
        getMessageListInteractor.execute(null, new Callback<List<MessageEntity>>() {

            @Override
            public void onAccept(List<MessageEntity> data) {
                view.refreshMessageList(data);
            }

            @Override
            public void onError(Throwable throwable) {
                view.handleException(throwable);
            }
        });
    }
}
