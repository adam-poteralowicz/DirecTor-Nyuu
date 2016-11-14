package com.apap.director.im.dao;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

import java.io.IOException;

public class DaoGenerator extends org.greenrobot.greendao.generator.DaoGenerator {

    public DaoGenerator() throws IOException {
    }

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.apap.director.im.dao.model");

        Entity contact = schema.addEntity("Contact");
        contact.addStringProperty("name").primaryKey();


        Entity conversation = schema.addEntity("Conversation");
        conversation.addStringProperty("sender");
        conversation.addStringProperty("recipient").primaryKey();


        Entity message = schema.addEntity("Message");
        message.addStringProperty("sender");
        message.addStringProperty("recipient");
        message.addStringProperty("content");
        message.addDateProperty("date");
//        message.addIdProperty().autoincrement().primaryKey();

        // one Conversation has "many" Messages
        Property conversationIdMessage = message.addStringProperty("conversationId").primaryKey().getProperty();
        ToMany conversationToMessages = conversation.addToMany(message, conversationIdMessage);
        conversationToMessages.setName("messages");

        // Conversation has "one" Contact
        Property contactIdProperty = conversation.addStringProperty("contactId").getProperty();
        conversation.addToOne(contact, contactIdProperty);

        new DaoGenerator().generateAll(schema, "../DirecTor-Nyuu/im/src/main/java");
    }
}
