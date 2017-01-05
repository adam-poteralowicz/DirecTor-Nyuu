package com.apap.director.client.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
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
import com.apap.director.db.realm.util.ArrayAdapterChangeListener;
import com.apap.director.manager.MessageManager;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NewMsgActivity extends Activity {

    @BindView(R.id.messengerField) EditText newMessageField;
    @BindView(R.id.newMsgRecipient) TextView recipient;
    @BindView(R.id.conversationView) ListView messagesView;
    ArrayAdapter<Message> arrayAdapter;
    private Long contactIdFromIntent;
    private List<Message> myMessages;
    private ArrayAdapterChangeListener<Message, RealmResults<Message>> changeListener;
    @Inject
    Realm realm;

    @Inject
    MessageManager messageManager;

    //TODO: Split this method

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ((App) getApplication()).getComponent().inject(this);
        setContentView(R.layout.new_msg_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("recipient") != null) {
            recipient.setText(getIntent().getStringExtra("recipient"));
        } else {
            recipient.setText(getIntent().getStringExtra("msgTitle"));
        }

        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);
        final Conversation conversation = realm.where(Conversation.class).equalTo("contact.id", contactIdFromIntent).findFirst();

        myMessages = messageManager.getMessages(conversation);
        if (myMessages != null) {
            arrayAdapter = new MessageAdapter(this, R.layout.item_chat_left, myMessages);
        }
        messagesView.setAdapter(arrayAdapter);

        RealmResults<Message> allMessages = realm.where(Message.class).equalTo("conversation.id", conversation.getId() ).findAll();
        changeListener = new ArrayAdapterChangeListener<>(arrayAdapter);

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
        messageManager.deleteMessage(messageId);
        return true;
    }


    public boolean onClick(View view) {

        String newMessage = String.valueOf(newMessageField.getText());
        String to = String.valueOf(recipient.getText());
        if ("".equals(newMessage))
            return false;

        realm.beginTransaction();
            Conversation conversation = realm.where(Conversation.class).equalTo("contact.id", contactIdFromIntent).findFirst();
            realm.copyToRealmOrUpdate(conversation);
        realm.commitTransaction();

        Message message = messageManager.addMessage(conversation, newMessage, to, true);

        realm.beginTransaction();
            myMessages.add(message);
        realm.commitTransaction();

        arrayAdapter.notifyDataSetChanged();
        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);
        newMessageField.setText("");
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}


