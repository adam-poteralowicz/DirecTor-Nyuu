package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.manager.DatabaseManager;
import com.apap.director.client.manager.IDatabaseManager;
import com.apap.director.im.dao.model.Conversation;

import java.util.ArrayList;
import java.util.List;

public class SingleContactActivity extends Activity {

    private IDatabaseManager databaseManager;
    List<String> myOptionsList = null;
    TextView contactNameView;
    ListView options;
    Intent intent;

    public void onCreate(Bundle savedInstanceState) {
        ((App) getApplication()).getDaoComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_contact_view);

        // init database manager
        databaseManager = new DatabaseManager(this);

        final String contactNameFromIntent = getIntent().getStringExtra("contactName");
        contactNameView = (TextView) findViewById(R.id.contactName);
        contactNameView.setText(contactNameFromIntent);

        options = (ListView) findViewById(R.id.optionsList);

        myOptionsList = new ArrayList<String>();
        myOptionsList.add("Send message");
        myOptionsList.add("Delete from contacts");
        myOptionsList.add("Return");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                myOptionsList);

        options.setAdapter(arrayAdapter);
        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(App.getContext(), myOptionsList.get(position), Toast.LENGTH_LONG).show();
                switch (position) {
                    case 0:
                    {
                        Long contactId = databaseManager.getContactByName(contactNameFromIntent).getId();
                        if (databaseManager.getConversationByContactId(contactId) == null) {
                            Conversation conversation = new Conversation();
                            conversation.setRecipient(contactNameFromIntent);
                            databaseManager.insertOrUpdateConversation(conversation);
                        }

                        intent = new Intent(App.getContext(), NewMsgActivity.class);
                        intent.putExtra("recipient", contactNameFromIntent);
                        startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        databaseManager.deleteContactByName(contactNameFromIntent);
                        intent = new Intent(App.getContext(), AuthUserActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:
                    {
                        intent = new Intent(App.getContext(), AuthUserActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

            Intent selectedIntent = new Intent(SingleContactActivity.this, AuthUserActivity.class);
            startActivityForResult(selectedIntent, 0011);

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
