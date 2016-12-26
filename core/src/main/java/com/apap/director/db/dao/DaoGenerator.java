package com.apap.director.db.dao;

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

        Schema schema = new Schema(1, "com.apap.director.db.dao.model");
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
        Entity dbIdentityKeyRecord = addDbIdentityKey(schema);
        Entity dbPreKeyRecord = addDbPreKey(schema);
        Entity dbSessionRecord = addDbSession(schema);
        Entity dbSignedPreKeyRecord = addDbSignedPreKey(schema);

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
        return message;
    }

    /**
     * Create identityKey's Properties
     *
     * @Return IdentityKey entity
     */
    private static Entity addDbIdentityKey(Schema schema) {
        Entity dbIdentityKey = schema.addEntity("DbIdentityKey");
        dbIdentityKey.addIdProperty().primaryKey().autoincrement();
        dbIdentityKey.addIntProperty("deviceId");
        dbIdentityKey.addStringProperty("name");
        dbIdentityKey.addByteArrayProperty("key");
        dbIdentityKey.addStringProperty("identityName");
        return dbIdentityKey;
    }

    /**
     * Create preKey's Properties
     *
     * @Return PreKey entity
     */
    private static Entity addDbPreKey(Schema schema) {
        Entity dbPreKey = schema.addEntity("DbPreKey");
        dbPreKey.addIdProperty().primaryKey().autoincrement();
        dbPreKey.addIntProperty("dbPreKeyId");
        dbPreKey.addByteArrayProperty("serialized");
        dbPreKey.addStringProperty("identityName");
        return dbPreKey;
    }

    /**
     * Create session's Properties
     *
     * @Return Session entity
     */
    private static Entity addDbSession(Schema schema) {
        Entity dbSession = schema.addEntity("DbSession");
        dbSession.addIdProperty().primaryKey().autoincrement();
        dbSession.addIntProperty("deviceId");
        dbSession.addStringProperty("name");
        dbSession.addByteArrayProperty("serialized");
        dbSession.addStringProperty("identityName");
        return dbSession;
    }

    /**
     * Create signedPreKey's Properties
     *
     * @Return SignedPreKey entity
     */
    private static Entity addDbSignedPreKey(Schema schema) {
        Entity dbSignedPreKey = schema.addEntity("DbSignedPreKey");
        dbSignedPreKey.addIdProperty().primaryKey().autoincrement();
        dbSignedPreKey.addIntProperty("dbSignedPreKeyId");
        dbSignedPreKey.addByteArrayProperty("serialized");
        dbSignedPreKey.addStringProperty("identityName");
        return dbSignedPreKey;
    }
}
