package com.apap.director.client.presentation.ui.inbox.presenter;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.mapper.ConversationMapper;
import com.apap.director.client.domain.interactor.inbox.GetConversationListInteractor;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.inbox.contract.InboxContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmResults;

/**
 * Created by Adam Poterałowicz
 */

public class InboxPresenter implements BasePresenter, InboxContract.Presenter {

    private InboxContract.View view;
    private GetConversationListInteractor getConversationListInteractor;

    private CompositeDisposable subscriptions;
    private ConversationMapper conversationMapper;

    @Inject
    public InboxPresenter(InboxContract.View view, GetConversationListInteractor getConversationListInteractor) {
        this.view = view;
        this.getConversationListInteractor = getConversationListInteractor;
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void getConversations(@Nullable final RealmResults<ConversationEntity> conversations) {
        subscriptions.add(getConversationListInteractor.execute(null)
                .subscribe(conversationModels -> {
                    List<ConversationEntity> entities = new ArrayList<>();

                    for (int i = 0; i < conversationModels.size(); i++) {
                        entities.add(conversationMapper.mapToEntity(conversationModels.get(i)));
                    }

                    if (conversations == null)
                        view.refreshConversationList(entities);
                    else
                        view.addChangeListener(entities);
                }, throwable -> view.handleException(throwable)));
    }
}
