package com.apap.director.client.presentation.ui.message;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.data.manager.MessageManager;
import com.apap.director.client.presentation.ui.home.adapter.MessageAdapter;
import com.apap.director.client.data.net.service.ClientService;
import com.apap.director.client.presentation.ui.listener.ArrayAdapterChangeListener;

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

    @Inject
    Realm realm;
    @Inject
    MessageManager messageManager;
    @Inject
    ConversationManager conversationManager;

    @BindView(R.id.messengerField)
    EditText newMessageField;
    @BindView(R.id.newMsgRecipient)
    TextView recipient;
    @BindView(R.id.conversationView)
    ListView messagesView;

    private Long contactIdFromIntent;
    private List<MessageEntity> myMessages;
    private ArrayAdapterChangeListener<MessageEntity, RealmResults<MessageEntity>> changeListener;
    private RealmResults<MessageEntity> allMessages;
    private String TAG = this.getClass().getSimpleName();

    //TODO: Split this method

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getComponent().inject(this);

        ClientService.sendMessage("NewMsgActivity");
        setContentView(R.layout.new_msg_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("recipient") != null) {
            recipient.setText(getIntent().getStringExtra("recipient"));
        } else {
            recipient.setText(getIntent().getStringExtra("msgTitle"));
        }

        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);

        myMessages = new ArrayList<>();
        ArrayAdapter<MessageEntity> arrayAdapter = new MessageAdapter(this, R.layout.item_chat_left, myMessages);
        messagesView.setAdapter(arrayAdapter);

        allMessages = realm.where(MessageEntity.class).equalTo("conversation.id", contactIdFromIntent).findAll();
        myMessages.addAll(allMessages);
        arrayAdapter.notifyDataSetChanged();

        changeListener = new ArrayAdapterChangeListener<>(arrayAdapter, "message activity listener");
        allMessages.addChangeListener(changeListener);

        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);

    }

    @Override
    protected void onDestroy() {
        allMessages.removeChangeListener(changeListener);
        super.onDestroy();
        realm.close();
    }

    @OnClick(R.id.sendButton)
    public void onClick() {
        String newMessage = String.valueOf(newMessageField.getText());
        if ("".equals(newMessage))
            return;

        ConversationEntity conversation = conversationManager.getConversationByContactId(contactIdFromIntent);

        ClientService.sendEncryptedMessage(conversation.getContact().getContactKeys().get(0).getKeyBase64(), realm.where(AccountEntity.class).equalTo("active", true).findFirst().getKeyBase64(), newMessage);
        newMessageField.setText("");
    }

    @OnItemLongClick(R.id.conversationView)
    public boolean deleteMessage(int position) {
        Log.v(TAG, "Deleting message, position: " + position);
        Long messageId = myMessages.get(position).getId();
        messageManager.deleteMessage(messageId);
        return true;
    }

}


