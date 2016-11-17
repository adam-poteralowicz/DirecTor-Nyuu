package com.apap.director.client.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.apap.director.im.dao.model.Contact;
import com.apap.director.im.dao.model.ContactDao;
import com.apap.director.im.dao.model.Conversation;
import com.apap.director.im.dao.model.ConversationDao;
import com.apap.director.im.dao.model.DaoMaster;
import com.apap.director.im.dao.model.DaoSession;
import com.apap.director.im.dao.model.Message;
import com.apap.director.im.dao.model.MessageDao;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Octa
 */
public class DatabaseManager implements IDatabaseManager, AsyncOperationListener {

    /**
     * Class tag. Used for debug.
     */
    private static final String TAG = DatabaseManager.class.getCanonicalName();
    /**
     * Instance of DatabaseManager
     */
    private static DatabaseManager instance;
    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private Context context;
    private DaoMaster.OpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link android.content.Context}.
     */
    public DatabaseManager(final Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, "sample-database", null);
        completedOperations = new CopyOnWriteArrayList<AsyncOperation>();
    }

    /**
     * @param context The Android {@link android.content.Context}.
     * @return this.instance
     */
    public static DatabaseManager getInstance(Context context) {

        if (instance == null) {
            instance = new DatabaseManager(context);
        }

        return instance;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    private void assertWaitForCompletion1Sec() {
        asyncSession.waitForCompletion(1000);
        asyncSession.isCompleted();
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        if (mHelper == null)
            mHelper = new DaoMaster.DevOpenHelper(this.context, "sample-database", null);
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        if (mHelper == null)
            mHelper = new DaoMaster.DevOpenHelper(this.context, "sample-database", null);
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public synchronized void dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // drops all tables
            mHelper.onCreate(database);              // creates the tables
            asyncSession.deleteAll(Contact.class);    // clear all elements from a table
            asyncSession.deleteAll(Conversation.class);
            asyncSession.deleteAll(Message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Contact insertContact(Contact contact) {
        try {
            if (contact != null) {
                openWritableDb();
                ContactDao contactDao = daoSession.getContactDao();
                contactDao.insert(contact);
                Log.d(TAG, "Inserted contact: " + contact.getName() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }

    @Override
    public synchronized ArrayList<Contact> listContacts() {
        List<Contact> contacts = null;
        try {
            openReadableDb();
            ContactDao contactDao = daoSession.getContactDao();
            contacts = contactDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contacts != null) {
            return new ArrayList<>(contacts);
        }
        return null;
    }

    @Override
    public synchronized void updateContact(Contact contact) {
        try {
            if (contact != null) {
                openWritableDb();
                daoSession.update(contact);
                Log.d(TAG, "Updated contact: " + contact.getName() + " from the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteContactByName(String name) {
        try {
            openWritableDb();
            ContactDao contactDao = daoSession.getContactDao();
            QueryBuilder<Contact> queryBuilder = contactDao.queryBuilder().where(ContactDao.Properties.Name.eq(name));
            List<Contact> contactToDelete = queryBuilder.list();
            for (Contact contact : contactToDelete) {
                contactDao.delete(contact);
            }
            daoSession.clear();
            Log.d(TAG, contactToDelete.size() + " entry. " + "Deleted contact: " + name + " from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean deleteContactById(Long contactId) {
        try {
            openWritableDb();
            ContactDao contactDao = daoSession.getContactDao();
            contactDao.deleteByKey(contactId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized Contact getContactById(Long contactId) {
        Contact contact = null;
        try {
            openReadableDb();
            ContactDao contactDao = daoSession.getContactDao();
            contact = contactDao.loadDeep(contactId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }

    @Override
    public synchronized void deleteContacts() {
        try {
            openWritableDb();
            ContactDao contactDao = daoSession.getContactDao();
            contactDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all contacts from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Contact getContactByName(String name) {
        Contact contact = null;
        try {
            openReadableDb();
            ContactDao contactDao = daoSession.getContactDao();
            contact = contactDao.queryBuilder().where(ContactDao.Properties.Name.eq(name)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }

    // Chyba jest zle
    @Override
    public synchronized Conversation getConversationByContactId(Long contactId) {
        Conversation conversation = null;
        try {
            openReadableDb();
            ConversationDao conversationDao = daoSession.getConversationDao();
            conversation = conversationDao.loadDeep(contactId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conversation;
    }

    @Override
    public synchronized Conversation insertOrUpdateConversation(Conversation conversation) {
        try {
            if (conversation != null) {
                openWritableDb();
                daoSession.insertOrReplace(conversation);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conversation;
    }

    @Override
    public synchronized void deleteConversationByRecipient(String recipient) {
        try {
            openWritableDb();
            ConversationDao dao = daoSession.getConversationDao();
            WhereCondition condition = ConversationDao.Properties.Recipient.eq(recipient);
            QueryBuilder<Conversation> queryBuilder = dao.queryBuilder().where(condition);
            dao.deleteInTx(queryBuilder.list());
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized ArrayList<Conversation> listConversations() {
        List<Conversation> conversations = null;
        try {
            openReadableDb();
            ConversationDao conversationDao = daoSession.getConversationDao();
            conversations = conversationDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (conversations != null) {
            return new ArrayList<>(conversations);
        }
        return null;
    }

    @Override
    public synchronized void insertOrUpdateMessage(Message message) {
        try {
            if (message != null) {
                openWritableDb();
                daoSession.insertOrReplace(message);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void bulkInsertMessages(Set<Message> messages) {
        try {
            if (messages != null && messages.size() > 0) {
                openWritableDb();
                asyncSession.insertOrReplaceInTx(Message.class, messages);
                assertWaitForCompletion1Sec();
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteMessageById(Long messageId) {
        try {
            openWritableDb();
            MessageDao messageDao = daoSession.getMessageDao();
            messageDao.deleteByKey(messageId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

