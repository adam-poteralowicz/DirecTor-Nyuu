package com.apap.director.db.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.apap.director.db.dao.model.Contact;
import com.apap.director.db.dao.model.ContactDao;
import com.apap.director.db.dao.model.Conversation;
import com.apap.director.db.dao.model.ConversationDao;
import com.apap.director.db.dao.model.DaoMaster;
import com.apap.director.db.dao.model.DaoSession;
import com.apap.director.db.dao.model.DbIdentityKey;
import com.apap.director.db.dao.model.DbIdentityKeyDao;
import com.apap.director.db.dao.model.DbPreKey;
import com.apap.director.db.dao.model.DbPreKeyDao;
import com.apap.director.db.dao.model.DbSession;
import com.apap.director.db.dao.model.DbSessionDao;
import com.apap.director.db.dao.model.DbSignedPreKey;
import com.apap.director.db.dao.model.DbSignedPreKeyDao;
import com.apap.director.db.dao.model.Message;
import com.apap.director.db.dao.model.MessageDao;

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

    @Override
    public synchronized void deleteDbIdentityKeys() {
        try {
            openWritableDb();
            DbIdentityKeyDao dbIdentityKeyDao = daoSession.getDbIdentityKeyDao();
            dbIdentityKeyDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all db identity keys from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbIdentityKeyById(Long id) {
        try {
            openWritableDb();
            DbIdentityKeyDao dbIdentityKeyDao = daoSession.getDbIdentityKeyDao();
            dbIdentityKeyDao.deleteByKey(id);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbIdentityKeyByDeviceId(int deviceId) {
        try {
            openWritableDb();
            DbIdentityKeyDao dao = daoSession.getDbIdentityKeyDao();
            WhereCondition condition = DbIdentityKeyDao.Properties.DeviceId.eq(deviceId);
            QueryBuilder<DbIdentityKey> queryBuilder = dao.queryBuilder().where(condition);
            dao.deleteInTx(queryBuilder.list());
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbIdentityKeyByName(String name) {
        try {
            openWritableDb();
            DbIdentityKeyDao dao = daoSession.getDbIdentityKeyDao();
            WhereCondition condition = DbIdentityKeyDao.Properties.Name.eq(name);
            QueryBuilder<DbIdentityKey> queryBuilder = dao.queryBuilder().where(condition);
            dao.deleteInTx(queryBuilder.list());
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized DbIdentityKey insertOrUpdateDbIdentityKey(DbIdentityKey dbIdentityKey) {
        try {
            if (dbIdentityKey != null) {
                openWritableDb();
                daoSession.insertOrReplace(dbIdentityKey);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbIdentityKey;
    }

    @Override
    public synchronized ArrayList<DbIdentityKey> listDbIdentityKeys() {
        List<DbIdentityKey> dbIdentityKeys = null;
        try {
            openReadableDb();
            DbIdentityKeyDao dbIdentityKeyDao = daoSession.getDbIdentityKeyDao();
            dbIdentityKeys = dbIdentityKeyDao.loadAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dbIdentityKeys != null) {
            return new ArrayList<>(dbIdentityKeys);
        }
        return null;
    }

    @Override
    public synchronized DbIdentityKey getDbIdentityKeyById(Long id) {
        DbIdentityKey dbIdentityKey = null;
        try {
            openReadableDb();
            DbIdentityKeyDao dbIdentityKeyDao = daoSession.getDbIdentityKeyDao();
            dbIdentityKey = dbIdentityKeyDao.queryBuilder().where(DbIdentityKeyDao.Properties.Id.eq(id)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbIdentityKey;
    }

    @Override
    public synchronized DbIdentityKey getDbIdentityKeyByDeviceId(int deviceId) {
        DbIdentityKey dbIdentityKey = null;
        try {
            openReadableDb();
            DbIdentityKeyDao identityKeyDao = daoSession.getDbIdentityKeyDao();
            dbIdentityKey = identityKeyDao.queryBuilder().where(DbIdentityKeyDao.Properties.DeviceId.eq(deviceId)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbIdentityKey;
    }

    @Override
    public synchronized DbIdentityKey getDbIdentityKeyByName(String name) {
        DbIdentityKey dbIdentityKey = null;
        try {
            openReadableDb();
            DbIdentityKeyDao dbIdentityKeyDao = daoSession.getDbIdentityKeyDao();
            dbIdentityKey = dbIdentityKeyDao.queryBuilder().where(DbIdentityKeyDao.Properties.Name.eq(name)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbIdentityKey;
    }

    @Override
    public synchronized void deleteDbPreKeys() {
        try {
            openWritableDb();
            DbPreKeyDao dbPreKeyDao = daoSession.getDbPreKeyDao();
            dbPreKeyDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all db pre keys from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbPreKeyById(Long id) {
        try {
            openWritableDb();
            DbPreKeyDao dbPreKeyDao = daoSession.getDbPreKeyDao();
            dbPreKeyDao.deleteByKey(id);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbPreKeyByDbPreKeyId(int dbPreKeyId) {
        try {
            openWritableDb();
            DbPreKeyDao dao = daoSession.getDbPreKeyDao();
            WhereCondition condition = DbPreKeyDao.Properties.DbPreKeyId.eq(dbPreKeyId);
            QueryBuilder<DbPreKey> queryBuilder = dao.queryBuilder().where(condition);
            dao.deleteInTx(queryBuilder.list());
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized DbPreKey insertOrUpdateDbPreKey(DbPreKey dbPreKey) {
        try {
            if (dbPreKey != null) {
                openWritableDb();
                daoSession.insertOrReplace(dbPreKey);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbPreKey;
    }

    @Override
    public synchronized ArrayList<DbPreKey> listDbPreKeys() {
        List<DbPreKey> dbPreKeys = null;
        try {
            openReadableDb();
            DbPreKeyDao dbPreKeyDao = daoSession.getDbPreKeyDao();
            dbPreKeys = dbPreKeyDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dbPreKeys != null) {
            return new ArrayList<>(dbPreKeys);
        }
        return null;
    }

    @Override
    public synchronized DbPreKey getDbPreKeyById(Long id) {
        DbPreKey dbPreKey = null;
        try {
            openReadableDb();
            DbPreKeyDao preKeyDao = daoSession.getDbPreKeyDao();
            dbPreKey = preKeyDao.queryBuilder().where(DbPreKeyDao.Properties.Id.eq(id)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbPreKey;
    }

    @Override
    public synchronized DbPreKey getDbPreKeyByDbPreKeyId(int preKeyId) {
        DbPreKey dbPreKey = null;
        try {
            openReadableDb();
            DbPreKeyDao preKeyDao = daoSession.getDbPreKeyDao();
            dbPreKey = preKeyDao.queryBuilder().where(DbPreKeyDao.Properties.DbPreKeyId.eq(preKeyId)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbPreKey;
    }

    @Override
    public synchronized void deleteDbSignedPreKeys() {
        try {
            openWritableDb();
            DbSignedPreKeyDao dbSignedPreKeyDao = daoSession.getDbSignedPreKeyDao();
            dbSignedPreKeyDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all db signed pre keys from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbSignedPreKeyById(Long id) {
        try {
            openWritableDb();
            DbSignedPreKeyDao dbSignedPreKeyDao = daoSession.getDbSignedPreKeyDao();
            dbSignedPreKeyDao.deleteByKey(id);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbSignedPreKeyByDbSignedPreKeyId(int dbSignedPreKeyId) {
        try {
            openWritableDb();
            DbSignedPreKeyDao dao = daoSession.getDbSignedPreKeyDao();
            WhereCondition condition = DbSignedPreKeyDao.Properties.DbSignedPreKeyId.eq(dbSignedPreKeyId);
            QueryBuilder<DbSignedPreKey> queryBuilder = dao.queryBuilder().where(condition);
            dao.deleteInTx(queryBuilder.list());
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized DbSignedPreKey insertOrUpdateDbSignedPreKey(DbSignedPreKey dbSignedPreKey) {
        try {
            if (dbSignedPreKey != null) {
                openWritableDb();
                daoSession.insertOrReplace(dbSignedPreKey);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbSignedPreKey;
    }

    @Override
    public synchronized ArrayList<DbSignedPreKey> listDbSignedPreKeys() {
        List<DbSignedPreKey> dbSignedPreKeys = null;
        try {
            openReadableDb();
            DbSignedPreKeyDao dbSignedPreKeyDao = daoSession.getDbSignedPreKeyDao();
            dbSignedPreKeys = dbSignedPreKeyDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dbSignedPreKeys != null) {
            return new ArrayList<>(dbSignedPreKeys);
        }
        return null;
    }

    @Override
    public synchronized DbSignedPreKey getDbSignedPreKeyById(Long id) {
        DbSignedPreKey dbSignedPreKey = null;
        try {
            openReadableDb();
            DbSignedPreKeyDao dbSignedPreKeyDao = daoSession.getDbSignedPreKeyDao();
            dbSignedPreKey = dbSignedPreKeyDao.queryBuilder().where(DbSignedPreKeyDao.Properties.Id.eq(id)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbSignedPreKey;
    }

    @Override
    public synchronized DbSignedPreKey getDbSignedPreKeyByDbSignedPreKeyId(int dbSignedPreKeyId) {
        DbSignedPreKey dbSignedPreKey = null;
        try {
            openReadableDb();
            DbSignedPreKeyDao dbSignedPreKeyDao = daoSession.getDbSignedPreKeyDao();
            dbSignedPreKey = dbSignedPreKeyDao.queryBuilder().where(DbSignedPreKeyDao.Properties.DbSignedPreKeyId.eq(dbSignedPreKeyId)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbSignedPreKey;
    }

    @Override
    public synchronized void deleteDbSessions() {
        try {
            openWritableDb();
            DbSessionDao dbSessionDao = daoSession.getDbSessionDao();
            dbSessionDao.deleteAll();
            daoSession.clear();
            Log.d(TAG, "Delete all db sessions from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbSessionById(Long id) {
        try {
            openWritableDb();
            DbSessionDao dbSessionDao = daoSession.getDbSessionDao();
            dbSessionDao.deleteByKey(id);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteDbSessionByName(String name) {
        try {
            openWritableDb();
            DbSessionDao dao = daoSession.getDbSessionDao();
            WhereCondition condition = DbSessionDao.Properties.Name.eq(name);
            QueryBuilder<DbSession> queryBuilder = dao.queryBuilder().where(condition);
            dao.deleteInTx(queryBuilder.list());
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized DbSession insertOrUpdateDbSession(DbSession dbSession) {
        try {
            if (dbSession != null) {
                openWritableDb();
                daoSession.insertOrReplace(dbSession);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbSession;
    }

    @Override
    public synchronized ArrayList<DbSession> listDbSessions() {
        List<DbSession> dbSessions = null;
        try {
            openReadableDb();
            DbSessionDao dbSessionDao = daoSession.getDbSessionDao();
            dbSessions = dbSessionDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dbSessions != null) {
            return new ArrayList<>(dbSessions);
        }
        return null;
    }

    @Override
    public synchronized DbSession getDbSessionById(Long id) {
        DbSession dbSession = null;
        try {
            openReadableDb();
            DbSessionDao dbSessionDao = daoSession.getDbSessionDao();
            dbSession = dbSessionDao.queryBuilder().where(DbSessionDao.Properties.Id.eq(id)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbSession;
    }

    @Override
    public synchronized DbSession getDbSessionByName(String name) {
        DbSession dbSession = null;
        try {
            openReadableDb();
            DbSessionDao dbSessionDao = daoSession.getDbSessionDao();
            dbSession = dbSessionDao.queryBuilder().where(DbSessionDao.Properties.Name.eq(name)).list().get(0);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbSession;
    }
}

