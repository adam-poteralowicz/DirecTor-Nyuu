package com.apap.director.client.data.db.mapper;

import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.domain.model.MessageModel;

import javax.inject.Inject;

/**
 * Created by Adam Poterałowicz
 */

public class MessageMapper extends BaseMapper<MessageModel, MessageEntity> {

    private ConversationMapper conversationMapper;

    @Inject
    public MessageMapper(ConversationMapper conversationMapper) {
        this.conversationMapper = conversationMapper;
    }

    @Override
    public MessageEntity mapToEntity(MessageModel model) {
        if (model == null)
            return null;

        MessageEntity entity = new MessageEntity();
        entity.setId(model.getId());
        entity.setDate(model.getDate());
        entity.setMine(model.getMine());
        entity.setConversation(conversationMapper.mapToEntity(model.getConversation()));
        entity.setContent(model.getContent());

        return entity;
    }

    @Override
    public MessageModel mapToModel(MessageEntity entity) {
        if (entity == null)
            return null;

        MessageModel model = new MessageModel();
        model.setId(entity.getId());
        model.setDate(entity.getDate());
        model.setMine(entity.getMine());
        model.setConversation(conversationMapper.mapToModel(entity.getConversation()));
        model.setContent(entity.getContent());

        return model;
    }
}
