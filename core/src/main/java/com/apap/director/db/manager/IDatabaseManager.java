package com.apap.director.db.manager;

import com.apap.director.db.dao.model.Contact;
import com.apap.director.db.dao.model.Conversation;
import com.apap.director.db.dao.model.DbIdentityKey;
import com.apap.director.db.dao.model.DbPreKey;
import com.apap.director.db.dao.model.DbSession;
import com.apap.director.db.dao.model.DbSignedPreKey;
import com.apap.director.db.dao.model.Message;

import java.util.ArrayList;
import java.util.Set;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author Octa
 */
public interface IDatabaseManager {

    /**
     * Closing available connections
     */
    void closeDbConnections();

    /**
     * Delete all tables and content from our database
     */
    void dropDatabase();

    /**
     * Insert a contact into the DB
     *
     * @param contact to be inserted
     */
    Contact insertContact(Contact contact);

    /**
     * List all the contacts from the DB
     *
     * @return list of contacts
     */
    ArrayList<Contact> listContacts();

    /**
     * Update a contact from the DB
     *
     * @param contact to be updated
     */
    void updateContact(Contact contact);

    /**
     * Delete all contact with a certain email from the DB
     *
     * @param name of contacts to be deleted
     */
    void deleteContactByName(String name);

    /**
     * Delete a contact with a certain id from the DB
     *
     * @param contactId of contacts to be deleted
     */
    boolean deleteContactById(Long contactId);

    /**
     * @param name - of the user we want to fetch
     * @return Return a contact by its name
     */
    Contact getContactByName(String name);

    /**
     * @param contactId - of the user we want to fetch
     * @return Return a contact by its id
     */
    Contact getContactById(Long contactId);

    /**
     * Delete all the contacts from the DB
     */
    void deleteContacts();

    /**
     *  @param contactId - of the user we want to fetch
     * @return a conversation by its id
     */
    Conversation getConversationByContactId(Long contactId);

    /**
     * Insert or update a conversation object into the DB
     *
     * @param conversation to be inserted/updated
     */
    Conversation insertOrUpdateConversation(Conversation conversation);

    /**
     * Delete a conversation by recipient
     */
    void deleteConversationByRecipient(String recipient);

    /**
     * List all the contacts from the DB
     *
     * @return list of contacts
     */
    ArrayList<Conversation> listConversations();

    /**
     * Insert or update a message object into the DB
     *
     * @param message to be inserted/updated
     */
    void insertOrUpdateMessage(Message message);

    /**
     * Insert or update a list of messages into the DB
     *
     * @param messages - list of objects
     */
    void bulkInsertMessages(Set<Message> messages);

    /**
     * Delete a message by its id
     */
    void deleteMessageById(Long id);

    /**
     * Delete all the identity keys from the DB
     */
    void deleteDbIdentityKeys();

    /**
     * Delete an identity key by its id
     */
    void deleteDbIdentityKeyById(Long id);

    /**
     * Delete an identity key by its deviceId
     */
    void deleteDbIdentityKeyByDeviceId(int deviceId);

    /**
     * Delete an identity key by its name
     */
    void deleteDbIdentityKeyByName(String name);

    /**
     * Insert or update an identity key object into the DB
     *
     * @param identityKey to be inserted/updated
     */
    DbIdentityKey insertOrUpdateDbIdentityKey(DbIdentityKey identityKey);

    /**
     * List all the identity keys from the DB
     *
     * @return list of identity keys
     */
    ArrayList<DbIdentityKey> listDbIdentityKeys();

    /**
     * @param id of an identity key we want to fetch
     * @return Return a identity key by its greendao id
     */
    DbIdentityKey getDbIdentityKeyById(Long id);

    /**
     * @param deviceId of an identity key we want to fetch
     * @return Return a identity key by its signal id
     */
    DbIdentityKey getDbIdentityKeyByDeviceId(int deviceId);

    /**
     * @param name of an identity key we want to fetch
     * @return Return a identity key by its name
     */
    DbIdentityKey getDbIdentityKeyByName(String name);

    /**
     * Delete all the db pre keys from the DB
     */
    void deleteDbPreKeys();

    /**
     * Delete a db pre key by its id
     */
    void deleteDbPreKeyById(Long id);

    /**
     * Delete a db pre key by its preKeyId
     */
    void deleteDbPreKeyByDbPreKeyId(int keyId);

    /**
     * Insert or update a pre key object into the DB
     *
     * @param dbPreKey to be inserted/updated
     */
    DbPreKey insertOrUpdateDbPreKey(DbPreKey dbPreKey);

    /**
     * List all the db pre keys from the DB
     *
     * @return list of db pre keys
     */
    ArrayList<DbPreKey> listDbPreKeys();

    /**
     * @param id of a db pre key we want to fetch
     * @return Return a db pre key by its greendao id
     */
    DbPreKey getDbPreKeyById(Long id);

    /**
     * @param dbPreKeyId of a db pre key we want to fetch
     * @return Return a db pre key by its signal id
     */
    DbPreKey getDbPreKeyByDbPreKeyId(int dbPreKeyId);

    /**
     * Delete all the db signed pre keys from the DB
     */
    void deleteDbSignedPreKeys();

    /**
     * Delete a db signed pre key by its id
     */
    void deleteDbSignedPreKeyById(Long id);

    /**
     * Delete a db signed pre key by its signedPreKeyId
     */
    void deleteDbSignedPreKeyByDbSignedPreKeyId(int keyId);

    /**
     * Insert or update a db signed pre key object into the DB
     *
     * @param dbSignedPreKey to be inserted/updated
     */
    DbSignedPreKey insertOrUpdateDbSignedPreKey(DbSignedPreKey dbSignedPreKey);

    /**
     * List all the db signed pre keys from the DB
     *
     * @return list of db signed pre keys
     */
    ArrayList<DbSignedPreKey> listDbSignedPreKeys();

    /**
     * @param id of a db signed pre key we want to fetch
     * @return Return a db signed pre key by its greendao id
     */
    DbSignedPreKey getDbSignedPreKeyById(Long id);

    /**
     * @param dbSignedPreKeyId of a db signed pre key we want to fetch
     * @return Return a db signed pre key by its signal id
     */
    DbSignedPreKey getDbSignedPreKeyByDbSignedPreKeyId(int dbSignedPreKeyId);

    /**
     * Delete all the db sessions from the DB
     */
    void deleteDbSessions();

    /**
     * Delete a db session by its id
     */
    void deleteDbSessionById(Long id);

    /**
     * Delete a db session by its name
     */
    void deleteDbSessionByName(String name);

    /**
     * Insert or update a db session object into the DB
     *
     * @param dbSession to be inserted/updated
     */
    DbSession insertOrUpdateDbSession(DbSession dbSession);

    /**
     * List all the db sessions from the DB
     *
     * @return list of db sessions
     */
    ArrayList<DbSession> listDbSessions();

    /**
     * @param dbSessionId of a db session we want to fetch
     * @return Return a db session by its id
     */
    DbSession getDbSessionById(Long dbSessionId);

    /**
     * @param dbSessionName of a session we want to fetch
     * @return Return a session by its name
     */
    DbSession getDbSessionByName(String dbSessionName);

    ArrayList<DbSession> listDbSessionsByName(String name);

    ArrayList<DbIdentityKey> listDbIdentityKeysByName(String name);

    void deleteDbSession(DbSession dbSession);

    void deleteDbSessionByDeviceIdAndName(Integer deviceId, String name);
}