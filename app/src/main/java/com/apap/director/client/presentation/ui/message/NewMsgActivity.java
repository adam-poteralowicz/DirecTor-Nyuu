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
import com.apap.director.client.data.net.service.ClientService;
import com.apap.director.client.domain.repository.MessageRepository;
import com.apap.director.client.presentation.ui.home.adapter.MessageAdapter;
import com.apap.director.client.presentation.ui.listener.ArrayAdapterChangeListener;
import com.apap.director.client.presentation.ui.message.contract.NewMsgContract;
import com.apap.director.client.presentation.ui.message.di.component.DaggerNewMsgComponent;
import com.apap.director.client.presentation.ui.message.di.module.NewMsgContractModule;
import com.apap.director.client.presentation.ui.message.presenter.NewMsgPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class NewMsgActivity extends Activity implements NewMsgContract.View {

    @Inject
    Realm realm;
    @Inject
    MessageManager messageManager;
    @Inject
    ConversationManager conversationManager;
    @Inject
    NewMsgPresenter newMsgPresenter;

    @BindView(R.id.messengerField)
    EditText newMessageField;
    @BindView(R.id.newMsgRecipient)
    TextView recipient;
    @BindView(R.id.conversationView)
    ListView messagesView;

    private Long contactIdFromIntent;
    private List<MessageEntity> myMessages;
    private ArrayAdapter<MessageEntity> arrayAdapter;
    private String TAG = this.getClass().getSimpleName();
    private ArrayAdapterChangeListener<MessageEntity, RealmResults<MessageEntity>> changeListener;
    private RealmResults<MessageEntity> allMessages;
    private MessageRepository messageRepository;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_msg_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ClientService.sendMessage("NewMsgActivity");
        decorateMsgScreen();
        setUpInjection();
        ButterKnife.bind(this);
        autorefreshMessages();

        arrayAdapter = setUpAdapter();
        newMsgPresenter.getMessagesByContact(contactIdFromIntent);
        allMessages.addChangeListener(new ArrayAdapterChangeListener<>(arrayAdapter, "message activity listener"));
    }

    @Override
    protected void onDestroy() {
        allMessages.removeChangeListener(changeListener);
        super.onDestroy();
        realm.close();
    }

    @Override
    public void refreshMessageList(List<MessageEntity> newList) {
        arrayAdapter.clear();
        myMessages.addAll(newList);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
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

    private void setUpInjection() {
        DaggerNewMsgComponent.builder()
                .mainComponent(((App) getApplication()).getComponent())
                .newMsgContractModule(new NewMsgContractModule(this, messageRepository, contactIdFromIntent))
                .build()
                .inject(this);
    }

    private ArrayAdapter<MessageEntity> setUpAdapter() {
        myMessages = new ArrayList<>();
        arrayAdapter = new MessageAdapter(this, R.layout.item_chat_left, myMessages);
        messagesView.setAdapter(arrayAdapter);

        return arrayAdapter;
    }

    private void autorefreshMessages() {
        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);
    }

    private void decorateMsgScreen() {
        if (getIntent().getStringExtra("recipient") != null) {
            recipient.setText(getIntent().getStringExtra("recipient"));
        } else {
            recipient.setText(getIntent().getStringExtra("msgTitle"));
        }
        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);
    }
}


