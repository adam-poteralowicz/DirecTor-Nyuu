package com.apap.director.client.domain.repository;

import com.apap.director.client.data.db.entity.ConversationEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-05.
 */

public interface ConversationRepository {
    Observable<List<ConversationEntity>> getConversationList();
}
