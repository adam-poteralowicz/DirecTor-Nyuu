package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.service.DbConversationService;
import com.apap.director.client.domain.repository.ConversationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-05.
 */

public class ConversationRepositoryImpl implements ConversationRepository {

    private DbConversationService dbConversationService;

    @Inject
    public ConversationRepositoryImpl(DbConversationService dbConversationService) {
        this.dbConversationService = dbConversationService;
    }

    @Override
    public Observable<List<ConversationEntity>> getConversationList() {
        return null;
    }
}
