package com.apap.director.client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.adapter.MessageAdapter;
import com.apap.director.db.dao.model.Conversation;
import com.apap.director.db.dao.model.Message;
import com.apap.director.db.manager.DatabaseManager;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class NewMsgActivity extends Activity {

    @BindView(R.id.messengerField) EditText newMessageField;
    @BindView(R.id.newMsgRecipient) TextView recipient;
    @BindView(R.id.conversationView) ListView messagesView;
    @Inject public DatabaseManager databaseManager;
    ArrayAdapter<Message> arrayAdapter;
    private Long contactIdFromIntent;
    private List<Message> myMessages;

    //TODO: Split this method

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getDaoComponent().inject(this);
        setContentView(R.layout.new_msg_view);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("recipient") != null) {
            recipient.setText(getIntent().getStringExtra("recipient"));
        } else {
            recipient.setText(getIntent().getStringExtra("msgTitle"));
        }

        // init database manager
        databaseManager = new DatabaseManager(this);

        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);
        final Conversation conversation = databaseManager.getConversationByContactId(contactIdFromIntent);
        if (conversation == null)
            Log.d("conversation", "null");
        if (conversation.getMessages() != null)
            myMessages = conversation.getMessages();

        if (myMessages != null) {
            arrayAdapter = new MessageAdapter(this, R.layout.item_chat_left, myMessages);
        }

        messagesView.setAdapter(arrayAdapter);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<com.apap.director.db.realm.model.Message> messages = realm.where(com.apap.director.db.realm.model.Message.class).findAll();
        messages.addChangeListener(new RealmChangeListener<RealmResults<com.apap.director.db.realm.model.Message>>() {
            @Override
            public void onChange(RealmResults<com.apap.director.db.realm.model.Message> results) {
                messages.size();
            }
        });
    }

    @OnItemLongClick(R.id.conversationView)
    public boolean deleteMessage(int position){
        Log.v("DTOR/NewMsgActivity", "Deleting message, position: "+position);
        Long messageId = myMessages.get(position).getId();
        databaseManager.deleteMessageById(messageId);
        myMessages.remove(position);
        arrayAdapter.notifyDataSetChanged();
        return true;
    }

    public void onClick(View view) {

        Conversation conversation = databaseManager.getConversationByContactId(contactIdFromIntent);
        Message message = new Message();
        message.setRecipient(String.valueOf(recipient.getText()));
        message.setDate(new Date());
        message.setMine(true);

        String messageText = String.valueOf(newMessageField.getText());
        if ("".equals(messageText)) {
            message.setContent("Sending empty message for fun!");
        } else {
            message.setContent(String.valueOf(newMessageField.getText()));
            Log.v("Message sent", String.valueOf(newMessageField.getText()));
        }

        conversation.setContactId(contactIdFromIntent);
        conversation.setRecipient(message.getRecipient());
        message.setConversationId(conversation.getId());

        myMessages.add(message);
        databaseManager.insertOrUpdateMessage(message);
        databaseManager.insertOrUpdateConversation(conversation);

        arrayAdapter.notifyDataSetChanged();
        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);
        newMessageField.setText("");
    }

}


