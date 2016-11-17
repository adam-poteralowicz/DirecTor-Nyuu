package com.apap.director.im.dao;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

import java.io.IOException;

public class DaoGenerator extends org.greenrobot.greendao.generator.DaoGenerator {

    private static final String OUT_DIR = "../DirecTor-Nyuu/im/src/main/java";

    public DaoGenerator() throws IOException {
    }

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.apap.director.im.dao.model");
        addTables(schema);
        new DaoGenerator().generateAll(schema, OUT_DIR);
    }

    /**
     * Create tables and the relationships between them
     */
    private static void addTables(Schema schema) {

        /* entities */
        Entity contact = addContact(schema);
        Entity conversation = addConversation(schema);
        Entity message = addMessage(schema);

        /* properties */
        Property contactIdForConversation = conversation.addLongProperty("contactId").notNull().getProperty();
        Property conversationIdForContact = contact.addLongProperty("conversationId").getProperty();
        Property conversationIdForMessage = message.addLongProperty("conversationId").notNull().getProperty();

        /* relationships between entities */
        conversation.addToOne(contact, contactIdForConversation, "contact"); // one-to-one (contact.getConversation)
        contact.addToOne(conversation, conversationIdForContact, "conversation"); //one-to-one (conversation.getContact)

        ToMany conversationToMessages = conversation.addToMany(message, conversationIdForMessage);
        conversationToMessages.setName("messages"); // one-to-many (conversation.getListOfMessages)

    }

    /**
     * Create contact's Properties
     *
     * @return Contact entity
     */
    private static Entity addContact(Schema schema) {
        Entity contact = schema.addEntity("Contact");
        contact.addIdProperty().primaryKey().autoincrement();
        contact.addStringProperty("name").notNull();
        return contact;
    }

    /**
     * Create conversation's Properties
     *
     * @return Conversation entity
     */
    private static Entity addConversation(Schema schema) {
        Entity conversation = schema.addEntity("Conversation");
        conversation.addIdProperty().primaryKey().autoincrement();
        conversation.addStringProperty("recipient").notNull();
        return conversation;
    }

    /**
     * Create message's Properties
     *
     * @return Message entity
     */
    private static Entity addMessage(Schema schema) {
        Entity message = schema.addEntity("Message");
        message.addIdProperty().primaryKey().autoincrement();
        message.addStringProperty("sender");
        message.addStringProperty("recipient").notNull();
        message.addStringProperty("content").notNull();
        message.addDateProperty("date").notNull();
        return message;
    }
}
