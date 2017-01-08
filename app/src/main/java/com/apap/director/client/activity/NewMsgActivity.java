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
import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;
import com.apap.director.db.realm.util.ArrayAdapterChangeListener;
import com.apap.director.im.websocket.service.ClientService;
import com.apap.director.manager.ConversationManager;
import com.apap.director.manager.MessageManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class NewMsgActivity extends Activity {

    @BindView(R.id.messengerField) EditText newMessageField;
    @BindView(R.id.newMsgRecipient) TextView recipient;
    @BindView(R.id.conversationView) ListView messagesView;
    ArrayAdapter<Message> arrayAdapter;
    private Long contactIdFromIntent;
    private List<Message> myMessages;
    private ArrayAdapterChangeListener<Message, RealmResults<Message>> changeListener;
    RealmResults<Message> allMessages;
    @Inject
    Realm realm;


    @Inject
    MessageManager messageManager;

    @Inject
    ConversationManager conversationManager;

    //TODO: Split this method

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getComponent().inject(this);

        ClientService.sendMessage("NewMsgActivity");
        setContentView(R.layout.new_msg_view);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("recipient") != null) {
            recipient.setText(getIntent().getStringExtra("recipient"));
        } else {
            recipient.setText(getIntent().getStringExtra("msgTitle"));
        }

        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);
        final Conversation conversation = conversationManager.getConversationByContactId(contactIdFromIntent);

        myMessages = new ArrayList<>();
        arrayAdapter = new MessageAdapter(this, R.layout.item_chat_left, myMessages);
        messagesView.setAdapter(arrayAdapter);

        allMessages = realm.where(Message.class).equalTo("conversation.id", contactIdFromIntent).findAll();
        myMessages.addAll(allMessages);
        arrayAdapter.notifyDataSetChanged();

        changeListener = new ArrayAdapterChangeListener<>(arrayAdapter, "message activity listener");
        allMessages.addChangeListener(changeListener);

        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);

    }

    @OnItemLongClick(R.id.conversationView)
    public boolean deleteMessage(int position){
        Log.v("DTOR/NewMsgActivity", "Deleting message, position: "+position);
        Long messageId = myMessages.get(position).getId();
        messageManager.deleteMessage(messageId);
        return true;
    }


    @OnClick(R.id.sendButton)
    public void onClick() {
        String newMessage = String.valueOf(newMessageField.getText());
        String to = String.valueOf(recipient.getText());
        if ("".equals(newMessage))
            return;


        Conversation conversation = conversationManager.getConversationByContactId(contactIdFromIntent);
        Message message = messageManager.addMessage(conversation, newMessage, to, true);

        ClientService.sendTestMessage(conversation.getContact().getContactKeys().get(0).getKeyBase64(),realm.where(Account.class).equalTo("active", true).findFirst().getKeyBase64(),newMessage);
        newMessageField.setText("");

    }

    @Override
    protected void onDestroy() {
        allMessages.removeChangeListener(changeListener);
        super.onDestroy();
        realm.close();
    }

}


