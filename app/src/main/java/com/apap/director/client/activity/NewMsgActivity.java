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
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;

import java.util.Date;
import java.util.List;

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
    ArrayAdapter<Message> arrayAdapter;
    private Long contactIdFromIntent;
    private List<Message> myMessages;
    private Realm realm;

    //TODO: Split this method

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getComponent().inject(this);
        setContentView(R.layout.new_msg_view);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        if (getIntent().getStringExtra("recipient") != null) {
            recipient.setText(getIntent().getStringExtra("recipient"));
        } else {
            recipient.setText(getIntent().getStringExtra("msgTitle"));
        }

        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);
        Conversation myConversation = realm.where(Conversation.class).equalTo("contact.id", contactIdFromIntent).findFirst();
        if (myConversation == null)
            Log.d("DTOR","MY CONVERSATION IS NULL");
        final Conversation conversation = realm.where(Conversation.class).equalTo("contact.id", contactIdFromIntent).findFirst();
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
        final RealmResults<Message> messages = realm.where(Message.class).findAll();
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
        realm.beginTransaction();
            realm.where(Message.class).equalTo("id", messageId).findFirst().deleteFromRealm();
        realm.commitTransaction();
        arrayAdapter.notifyDataSetChanged();
        return true;
    }

    public void onClick(View view) {

        realm.beginTransaction();
            Conversation conversation = realm.where(Conversation.class).equalTo("contact.id", contactIdFromIntent).findFirst();
            Message message = realm.createObject(Message.class, generateMessageId());
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

            conversation.setContact(realm.where(Contact.class).equalTo("id", contactIdFromIntent).findFirst());
            message.setConversation(conversation);
            realm.copyToRealmOrUpdate(conversation);
            realm.copyToRealmOrUpdate(message);
        myMessages.add(message);
        realm.commitTransaction();

        arrayAdapter.notifyDataSetChanged();
        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);
        newMessageField.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    /**
     *
     * @return id for new Realm Message object
     */
    public long generateMessageId() {
        long id;
        try {
            if (realm.where(Message.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(Message.class).max("id").longValue() + 1;
            }
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

}


