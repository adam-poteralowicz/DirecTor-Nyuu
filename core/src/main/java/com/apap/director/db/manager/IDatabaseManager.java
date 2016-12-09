package com.apap.director.db.manager;

import com.apap.director.db.dao.model.Contact;
import com.apap.director.db.dao.model.Conversation;
import com.apap.director.db.dao.model.IdentityKey;
import com.apap.director.db.dao.model.Message;
import com.apap.director.db.dao.model.PreKey;
import com.apap.director.db.dao.model.Session;
import com.apap.director.db.dao.model.SignedPreKey;

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
    void deleteIdentityKeys();

    /**
     * Delete an identity key by its id
     */
    void deleteIdentityKeyById(Long id);

    /**
     * Delete an identity key by its identityKeyId
     */
    void deleteIdentityKeyByIdentityKeyId(int keyId);

    /**
     * Delete an identity key by its name
     */
    void deleteIdentityKeyByName(String name);

    /**
     * Insert or update an identity key object into the DB
     *
     * @param identityKey to be inserted/updated
     */
    IdentityKey insertOrUpdateIdentityKey(IdentityKey identityKey);

    /**
     * List all the identity keys from the DB
     *
     * @return list of identity keys
     */
    ArrayList<IdentityKey> listIdentityKeys();

    /**
     * @param id of an identity key we want to fetch
     * @return Return a identity key by its greendao id
     */
    IdentityKey getIdentityKeyById(Long id);

    /**
     * @param identityKeyId of an identity key we want to fetch
     * @return Return a identity key by its signal id
     */
    IdentityKey getIdentityKeyByIdentityKeyId(int identityKeyId);

    /**
     * @param name of an identity key we want to fetch
     * @return Return a identity key by its name
     */
    IdentityKey getIdentityKeyByName(String name);

    /**
     * Delete all the pre keys from the DB
     */
    void deletePreKeys();

    /**
     * Delete a pre key by its id
     */
    void deletePreKeyById(Long id);

    /**
     * Delete a pre key by its preKeyId
     */
    void deletePreKeyByPreKeyId(int keyId);

    /**
     * Insert or update a pre key object into the DB
     *
     * @param preKey to be inserted/updated
     */
    PreKey insertOrUpdatePreKey(PreKey preKey);

    /**
     * List all the pre keys from the DB
     *
     * @return list of pre keys
     */
    ArrayList<PreKey> listPreKeys();

    /**
     * @param id of a pre key we want to fetch
     * @return Return a pre key by its greendao id
     */
    PreKey getPreKeyById(Long id);

    /**
     * @param preKeyId of a pre key we want to fetch
     * @return Return a pre key by its signal id
     */
    PreKey getPreKeyByPreKeyId(int preKeyId);

    /**
     * Delete all the signed pre keys from the DB
     */
    void deleteSignedPreKeys();

    /**
     * Delete a signed pre key by its id
     */
    void deleteSignedPreKeyById(Long id);

    /**
     * Delete a signed pre key by its signedPreKeyId
     */
    void deleteSignedPreKeyBySignedPreKeyId(int keyId);

    /**
     * Insert or update a signed pre key object into the DB
     *
     * @param signedPreKey to be inserted/updated
     */
    SignedPreKey insertOrUpdateSignedPreKey(SignedPreKey signedPreKey);

    /**
     * List all the signed pre keys from the DB
     *
     * @return list of signed pre keys
     */
    ArrayList<SignedPreKey> listSignedPreKeys();

    /**
     * @param id of a signed pre key we want to fetch
     * @return Return a signed pre key by its greendao id
     */
    SignedPreKey getSignedPreKeyById(Long id);

    /**
     * @param signedPreKeyId of a signed pre key we want to fetch
     * @return Return a signed pre key by its signal id
     */
    SignedPreKey getSignedPreKeyBySignedPreKeyId(int signedPreKeyId);

    /**
     * Delete all the sessions from the DB
     */
    void deleteSessions();

    /**
     * Delete a session by its id
     */
    void deleteSessionById(Long id);

    /**
     * Delete a session by its name
     */
    void deleteSessionByName(String name);

    /**
     * Insert or update a session object into the DB
     *
     * @param session to be inserted/updated
     */
    Session insertOrUpdateSession(Session session);

    /**
     * List all the sessions from the DB
     *
     * @return list of sessions
     */
    ArrayList<Session> listSessions();

    /**
     * @param sessionId of a session we want to fetch
     * @return Return a session by its id
     */
    Session getSessionById(Long sessionId);

    /**
     * @param sessionName of a session we want to fetch
     * @return Return a session by its name
     */
    Session getSessionByName(String sessionName);
}