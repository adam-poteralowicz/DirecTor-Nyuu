package com.apap.director.client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.adapter.MessageAdapter;
import com.apap.director.director_db.manager.DatabaseManager;
import com.apap.director.director_db.manager.IDatabaseManager;
import com.apap.director.director_db.dao.model.Conversation;
import com.apap.director.director_db.dao.model.Message;
import com.apap.director.im.domain.chat.service.TCPChatService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewMsgActivity extends Activity {
    EditText newMessageField;
    TextView recipient;
    ListView messagesView;
    ArrayAdapter<Message> arrayAdapter;
    private IDatabaseManager databaseManager;
    private Long contactIdFromIntent;
    private List<Message> myMessages;

    public void onCreate(Bundle savedInstanceState) {
        ((App) getApplication()).getDaoComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_msg_view);

        messagesView = (ListView) findViewById(R.id.conversationView);
        newMessageField = (EditText) findViewById(R.id.messengerField);
        recipient = (TextView) findViewById(R.id.newMsgRecipient);
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
        messagesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Long messageId = myMessages.get(position).getId();
                databaseManager.deleteMessageById(messageId);
                myMessages.remove(position);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

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
    }

    /**
     * Called after your activity has been stopped, prior to it being started again.
     * Always followed by onStart()
     */
    @Override
    protected void onRestart() {
        if (databaseManager == null)
            databaseManager = new DatabaseManager(this);

        super.onRestart();
    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity
     * to start interacting with the user.
     */
    @Override
    protected void onResume() {
        // init database manager
        databaseManager = DatabaseManager.getInstance(this);

        super.onResume();
    }

    /**
     * Called when you are no longer visible to the user.
     */
    @Override
    protected void onStop() {
        if (databaseManager != null)
            databaseManager.closeDbConnections();

        super.onStop();
    }

}


