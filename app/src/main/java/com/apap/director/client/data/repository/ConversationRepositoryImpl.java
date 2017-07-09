package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.mapper.ConversationMapper;
import com.apap.director.client.data.db.service.DbConversationService;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.model.SessionModel;
import com.apap.director.client.domain.repository.ConversationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class ConversationRepositoryImpl implements ConversationRepository {

    private DbConversationService dbConversationService;
    private ConversationMapper conversationMapper;

    @Inject
    public ConversationRepositoryImpl(DbConversationService dbConversationService, ConversationMapper conversationMapper) {
        this.dbConversationService = dbConversationService;
        this.conversationMapper = conversationMapper;
    }

    @Override
    public Observable<List<ConversationModel>> getConversationList() {
        return Observable.just(conversationMapper.mapToList(conversationMapper, dbConversationService.getConversationList()));
    }

    @Override
    public Observable<Long> findLastId() {
        return Observable.just(dbConversationService.findNextId());
    }

    @Override
    public Observable<ConversationModel> addSession(ConversationModel conversationModel, SessionModel sessionModel) {
        conversationModel.setSession(sessionModel);
        return Observable.just(conversationModel);
    }

    @Override
    public Observable<Boolean> deleteConversation(ConversationModel conversationModel) {
        ConversationEntity entity = conversationMapper.mapToEntity(conversationModel);
        dbConversationService.deleteConversation(entity);
        return Observable.just(!entity.isValid());
    }

    @Override
    public Observable<ConversationModel> getConversation(Long ownerId) {
        ConversationEntity conversationEntity = dbConversationService.getConversation(ownerId);

        if(conversationEntity == null) {
            return Observable.empty();
        }
        else {
            return Observable.just(conversationMapper.mapToModel(conversationEntity));
        }
    }


}
