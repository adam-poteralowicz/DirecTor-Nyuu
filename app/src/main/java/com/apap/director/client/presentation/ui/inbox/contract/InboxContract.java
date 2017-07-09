package com.apap.director.client.presentation.ui.inbox.contract;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

import io.reactivex.annotations.Nullable;
import io.realm.RealmResults;

/**
 * Created by Adam Poterałowicz
 */

public interface InboxContract {

    interface View extends BaseView {

        void refreshConversationList(List<ConversationEntity> data);

        void addChangeListener(List<ConversationEntity> data);

        void handleSuccess(String message);
    }

    interface Presenter extends BasePresenter {
        void getConversations(@Nullable RealmResults<ConversationEntity> conversations);

        void deleteConversation(ConversationEntity conversationEntity);
    }
}
