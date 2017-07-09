package com.apap.director.client.domain.repository;

import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.model.SessionModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public interface ConversationRepository {

    Observable<List<ConversationModel>> getConversationList();

    Observable<Long> findLastId();

    Observable<ConversationModel> getConversation(Long ownerId);

    Observable<ConversationModel> addSession(ConversationModel conversationModel, SessionModel sessionModel);
}
