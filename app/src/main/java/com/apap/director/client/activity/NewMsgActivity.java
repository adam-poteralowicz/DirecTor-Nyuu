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
import com.apap.director.db.dao.model.Conversation;
import com.apap.director.db.dao.model.Message;
import com.apap.director.db.manager.DatabaseManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;

public class NewMsgActivity extends Activity {

    @BindView(R.id.messengerField) EditText newMessageField;
    @BindView(R.id.newMsgRecipient) TextView recipient;
    @BindView(R.id.conversationView) ListView messagesView;

    @Inject public DatabaseManager databaseManager;

    ArrayList<String> messages_list;
    ArrayAdapter<String> arrayAdapter;
    private Long contactId;
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

        messages_list = new ArrayList<String>();
        contactId = databaseManager.getContactByName(String.valueOf(recipient.getText())).getId();
            final Conversation conversation = databaseManager.getConversationByContactId(contactId);
            if (conversation == null)
                Log.d("conversation", "null");
            if (conversation.getMessages() != null)
                myMessages = conversation.getMessages();

            if (myMessages != null) {
                Log.v("Conversation messages #", Integer.toString(conversation.getMessages().size()));
                for (int i = 0; i < conversation.getMessages().size(); i++) {
                    messages_list.add(conversation.getMessages().get(i).getContent());
                }
            }

            arrayAdapter = new ArrayAdapter<String>(
                    App.getContext(),
                    android.R.layout.simple_list_item_1,
                    messages_list);
            messagesView.setAdapter(arrayAdapter);
    }

    @OnItemLongClick(R.id.conversationView)
    public boolean deleteMessage(int position){
        Log.v("DTOR/NewMsgActivity", "Deleting message, position: "+position);
        Long messageId = myMessages.get(position).getId();
        databaseManager.deleteMessageById(messageId);
        messages_list.remove(position);
        arrayAdapter.notifyDataSetChanged();
        return true;
    }

    public void onClick(View view) {

        Conversation conversation = databaseManager.getConversationByContactId(contactId);
        Message message = new Message();
        message.setRecipient(String.valueOf(recipient.getText()));
        message.setDate(new Date());

        String messageText = String.valueOf(newMessageField.getText());
        if ("".equals(messageText)) {
            messages_list.add("Sending empty message for fun!");
            message.setContent("Sending empty message for fun!");
        } else {
            messages_list.add(String.valueOf(newMessageField.getText()));
            message.setContent(String.valueOf(newMessageField.getText()));
            Log.v("Message sent", String.valueOf(newMessageField.getText()));
        }

        conversation.setContactId(contactId);
        conversation.setRecipient(message.getRecipient());
        message.setConversationId(conversation.getId());

        myMessages.add(message);
        databaseManager.insertOrUpdateMessage(message);
        databaseManager.insertOrUpdateConversation(conversation);

        arrayAdapter.notifyDataSetChanged();
        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);

    }

}


