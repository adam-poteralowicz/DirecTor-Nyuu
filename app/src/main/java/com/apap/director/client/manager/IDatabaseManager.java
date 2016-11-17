package com.apap.director.client.manager;

import com.apap.director.im.dao.model.Contact;
import com.apap.director.im.dao.model.Conversation;
import com.apap.director.im.dao.model.Message;

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


}