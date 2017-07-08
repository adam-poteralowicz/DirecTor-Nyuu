package com.apap.director.client.data.net.mapper;

import com.apap.director.client.data.net.mapper.base.BaseTOMapper;
import com.apap.director.client.data.net.model.Message;
import com.apap.director.client.data.net.to.MessageTO;

/**
 * Created by Adam Poterałowicz
 */

public class MessageTOMapper extends BaseTOMapper<MessageTO, Message> {

    @Override
    public Message mapToTO(MessageTO model) {
        if (model == null)
            return null;

        Message message = new Message();
        message.setType(model.getType());
        message.setFrom(model.getFrom());
        message.setMessage(model.getMessage());

        return message;
    }

    @Override
    public MessageTO mapToModel(Message to) {
        if (to == null)
            return null;

        MessageTO messageTO = new MessageTO();
        messageTO.setType(to.getType());
        messageTO.setFrom(to.getFrom());
        messageTO.setMessage(to.getMessage());

        return messageTO;
    }
}
