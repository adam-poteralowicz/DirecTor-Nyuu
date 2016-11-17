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
import com.apap.director.client.manager.DatabaseManager;
import com.apap.director.client.manager.IDatabaseManager;
import com.apap.director.im.dao.model.Conversation;
import com.apap.director.im.dao.model.Message;
import com.apap.director.im.domain.chat.service.TCPChatService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewMsgActivity extends Activity {
    EditText newMessageField;
    TextView recipient;
    ListView messagesView;
    ArrayList<String> messages_list;
    ArrayAdapter<String> arrayAdapter;
    private IDatabaseManager databaseManager;
    private Long contactId;
    private List<Message> myMessages;

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

        // init database manager
        databaseManager = new DatabaseManager(this);

        contactId = databaseManager.getContactByName(String.valueOf(recipient.getText())).getId();
        final Conversation conversation = databaseManager.getConversationByContactId(contactId);
        if (conversation.getMessages() != null)
            myMessages = conversation.getMessages();

            messages_list = new ArrayList<String>();
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
            messagesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Long messageId = myMessages.get(position).getId();
                    //databaseManager.deleteMessageById(conversation, msgid);
                    databaseManager.deleteMessageById(messageId);
                    messages_list.remove(position);
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
//        Log.v("HAI/NewMsgActivity", "Trying to bind...");
//        Intent intent = new Intent(this, TCPChatService.class);
//        bindService(intent, connection, Context.BIND_AUTO_CREATE);

//        DaoSession conversationDaoSession = ((App) getApplicationContext()).getConversationDaoSession();

        Conversation conversation = databaseManager.getConversationByContactId(contactId);

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
            Log.v("Message sent", String.valueOf(newMessageField.getText()));
        }

        conversation.setContactId(contactId);
        conversation.setRecipient(message.getRecipient());
        message.setConversationId(conversation.getId());

        databaseManager.insertOrUpdateMessage(message);
        messages.add(message);
        databaseManager.insertOrUpdateConversation(conversation);

        arrayAdapter.notifyDataSetChanged();
        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesView.setStackFromBottom(true);

//        if (chatService == null)
//            Toast.makeText(NewMsgActivity.this, "chat service null", Toast.LENGTH_LONG).show();
        //chatService.sendMessage(message.getRecipient(), message.getContent());

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


