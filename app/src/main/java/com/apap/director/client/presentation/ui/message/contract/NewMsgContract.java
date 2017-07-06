package com.apap.director.client.presentation.ui.message.contract;

import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

/**
 * Created by Adam on 2017-07-04.
 */

public interface NewMsgContract {

    interface View extends BaseView {
        void refreshMessageList(List<MessageEntity> data);
    }

    interface Presenter extends BasePresenter {
        void getMessagesByContact(Long contactIdFromIntent);
    }
}
