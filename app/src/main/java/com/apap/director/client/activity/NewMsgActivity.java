package com.apap.director.client.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.im.dao.model.MessageDao;
import com.apap.director.im.domain.chat.service.TCPChatService;
import com.apap.director.im.util.SimpleBinder;

import com.apap.director.im.dao.model.Conversation;
import com.apap.director.im.dao.model.ConversationDao;
import com.apap.director.im.dao.model.DaoSession;
import com.apap.director.im.dao.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class NewMsgActivity extends Activity {
    EditText newMessageField;
    TextView recipient;
    ListView messagesView;
    ArrayList<String> messages_list;
    ArrayAdapter<String> arrayAdapter;

    @Inject @Named("conversationDao") DaoSession conversationDaoSession;
    @Inject @Named("messageDao") DaoSession messageDaoSession;
    @Inject @Named("contactDao") DaoSession contactDaoSession;

    TCPChatService chatService;

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

        ConversationDao conversationDao = conversationDaoSession.getConversationDao();
        final Conversation conversation = conversationDao.load(String.valueOf(recipient.getText()));

        messages_list = new ArrayList<String>();
        if (!conversation.getMessages().isEmpty()) {
            for (int i = 0; i < conversation.getMessages().size(); i++) {
                Log.v("Conversation messages #", Integer.toString(conversation.getMessages().size()));
                messages_list.add(conversation.getMessages().get(i).getContent());
            }
        }
        arrayAdapter = new ArrayAdapter<String>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                messages_list);

        messagesView.setAdapter(arrayAdapter);

        messagesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                messages_list.remove(position);
                conversation.getMessages().remove(position);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    public void onClick(View view) {

//        ServiceConnection connection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                Log.v("HAI/ServiceConnection", "Connected");
//                Log.v("HAI", "HAI HAI HAI");
//                SimpleBinder binder = (SimpleBinder) service;
//                chatService = (TCPChatService) binder.getService();
//
//
//                Log.v("HAI/NewMsgActivity", "WAITED");
//                chatService.sendMessage("ejabberd@dev02.sagiton.pl", "hai from app");
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                Log.v("HAIServiceConnection", "Disconnected");
//            }
//
//        };
//
//
//
//        Log.v("HAI/NewMsgActivity", "Trying to bind...");
//        Intent intent = new Intent(this, TCPChatService.class);
//        bindService(intent, connection, Context.BIND_AUTO_CREATE);




//        DaoSession conversationDaoSession = ((App) getApplicationContext()).getConversationDaoSession();
        ConversationDao conversationDao = conversationDaoSession.getConversationDao();
        MessageDao messageDao = messageDaoSession.getMessageDao();

        //TODO: check if conversation exists

        Conversation conversation = new Conversation();
        List<Message> messages = conversation.getMessages();
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
        }

        conversation.setContactId(message.getRecipient());
        conversation.setContact(contactDaoSession.getContactDao().load(conversation.getContactId()));
        conversation.setRecipient(message.getRecipient());

        message.setConversationId(conversation.getContactId());
        messageDao.insert(message);
        messages.add(message);

        //conversation.getMessages().add(message); // Entity is detached from DAO context ; loadDeep(), queryDeep()
        //conversationDao.insertOrReplace(conversation);   // potrzebne?
        //conversationDao.save(conversation); // potrzebne?
        arrayAdapter.notifyDataSetChanged();

        // ----Set autoscroll of listview when a new message arrives----//
        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);

//        if (chatService == null)
//            Toast.makeText(NewMsgActivity.this, "chat service null", Toast.LENGTH_LONG).show();
        //chatService.sendMessage(message.getRecipient(), message.getContent());

    }

}


