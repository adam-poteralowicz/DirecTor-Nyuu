package com.apap.director.client.presentation.ui.message.contract;

import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

/**
 * Created by Adam Poterałowicz
 */

public interface NewMsgContract {

    interface View extends BaseView {

        void refreshMessageList(List<MessageEntity> data);

        void handleSuccess(String message);
    }

    interface Presenter extends BasePresenter {

        void getMessagesByContact(Long contactId);

        void deleteMessage(Long messageId);
    }
}
