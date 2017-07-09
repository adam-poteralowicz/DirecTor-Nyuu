package com.apap.director.client.domain.repository;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.domain.model.ConversationModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-05.
 */

public interface ConversationRepository {
    Observable<List<ConversationModel>> getConversationList();
    Observable<Long> findLastId();
}
