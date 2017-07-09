package com.apap.director.client.data.db.mapper;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.model.SessionModel;

import javax.inject.Inject;

/**
 * Created by Adam Poterałowicz
 */

public class ConversationMapper extends BaseMapper<ConversationModel, ConversationEntity> {

    private MessageMapper messageMapper;
    private ContactMapper contactMapper;

    @Inject
    public ConversationMapper(MessageMapper messageMapper, ContactMapper contactMapper) {
        this.messageMapper = messageMapper;
        this.contactMapper = contactMapper;
    }

    @Override
    public ConversationEntity mapToEntity(ConversationModel model) {
        if (model == null)
            return null;

        ConversationEntity entity = new ConversationEntity();
        entity.setId(model.getId());
        entity.setContact(contactMapper.mapToEntity(model.getContact()));
        entity.setMessages(mapToRealmList(messageMapper, model.getMessages()));

        return entity;
    }

    @Override
    public ConversationModel mapToModel(ConversationEntity entity) {
        if (entity == null)
            return null;

        ConversationModel model = new ConversationModel();
        model.setId(entity.getId());
        model.setContact(contactMapper.mapToModel(entity.getContact()));
        model.setMessages(mapToList(messageMapper, entity.getMessages()));

        return model;
    }
}
