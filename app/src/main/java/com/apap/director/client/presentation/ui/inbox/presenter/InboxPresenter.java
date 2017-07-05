package com.apap.director.client.presentation.ui.inbox.presenter;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.domain.interactor.base.Callback;
import com.apap.director.client.domain.interactor.inbox.GetConversationListInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.inbox.contract.InboxContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;
import io.realm.RealmResults;

/**
 * Created by Adam on 2017-07-05.
 */

public class InboxPresenter implements BasePresenter, InboxContract.Presenter {

    private InboxContract.View view;
    private GetConversationListInteractor getConversationListInteractor;

    @Inject
    public InboxPresenter(InboxContract.View view, GetConversationListInteractor getConversationListInteractor) {
        this.view = view;
        this.getConversationListInteractor = getConversationListInteractor;
    }

    @Override
    public void dispose() {
        getConversationListInteractor.dispose();
    }

    @Override
    public void getConversations(@Nullable final RealmResults<ConversationEntity> conversations) {
        getConversationListInteractor.execute(null, new Callback<List<ConversationEntity>>() {
            @Override
            public void onAccept(List<ConversationEntity> data) {
                if (conversations == null)
                    view.refreshConversationList(data);
                else
                    view.addChangeListener(data);
            }

            @Override
            public void onError(Throwable throwable) {
                view.handleException(throwable);
            }
        });
    }
}
