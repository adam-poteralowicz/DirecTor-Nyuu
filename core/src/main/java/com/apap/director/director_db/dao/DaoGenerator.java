package com.apap.director.director_db.dao;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

import java.io.IOException;

public class DaoGenerator extends org.greenrobot.greendao.generator.DaoGenerator {

    private static final String OUT_DIR = "../DirecTor-Nyuu/core/src/main/java";

    public DaoGenerator() throws IOException {
    }

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.apap.director.director_db.dao.model");
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
        Entity identityKeyRecord = addIdentityKey(schema);
        Entity preKeyRecord = addPreKey(schema);
        Entity sessionRecord = addSession(schema);
        Entity signedPreKeyRecord = addSignedPreKey(schema);

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
        contact.addStringProperty("image");
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
        message.addBooleanProperty("mine");
        return message;
    }

    /**
     * Create identityKey's Properties
     *
     * @Return IdentityKey entity
     */
    private static Entity addIdentityKey(Schema schema) {
        Entity identityKey = schema.addEntity("IdentityKey");
        identityKey.addIdProperty().primaryKey().autoincrement();
        identityKey.addIntProperty("identityKeyId");
        identityKey.addStringProperty("name");
        identityKey.addByteArrayProperty("serialized");
        return identityKey;
    }

    /**
     * Create preKey's Properties
     *
     * @Return PreKey entity
     */
    private static Entity addPreKey(Schema schema) {
        Entity preKey = schema.addEntity("PreKey");
        preKey.addIdProperty().primaryKey().autoincrement();
        preKey.addIntProperty("preKeyId");
        preKey.addByteArrayProperty("serialized");
        return preKey;
    }

    /**
     * Create session's Properties
     *
     * @Return Session entity
     */
    private static Entity addSession(Schema schema) {
        Entity session = schema.addEntity("Session");
        session.addIdProperty().primaryKey().autoincrement();
        session.addStringProperty("name");
        session.addByteArrayProperty("serialized");
        return session;
    }

    /**
     * Create signedPreKey's Properties
     *
     * @Return SignedPreKey entity
     */
    private static Entity addSignedPreKey(Schema schema) {
        Entity signedPreKey = schema.addEntity("SignedPreKey");
        signedPreKey.addIdProperty().primaryKey().autoincrement();
        signedPreKey.addIntProperty("signedPreKeyId");
        signedPreKey.addByteArrayProperty("serialized");
        return signedPreKey;
    }
}
