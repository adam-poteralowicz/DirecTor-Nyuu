package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.manager.ContactManager;
import com.apap.director.manager.ConversationManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class NewContactActivity extends Activity {

    @Inject
    ContactManager
            contactManager;
    @Inject
    ConversationManager
            conversationManager;

    @BindView(R.id.contactNameEditText)
    EditText contactNameEditText;
    @BindView(R.id.theirPublicKey)
    TextView textView;

    private String contactPublicKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contact_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);
        contactPublicKey = getIntent().getExtras().getString("key");
        textView.setText("Contact public key: " + contactPublicKey);
    }

    @OnClick(R.id.saveContactButton)
    public void addContact() {

        if (contactNameEditText.getText().length() == 0) {
            Toast.makeText(this, "Type a valid name", Toast.LENGTH_SHORT);
            return;
        }

        String name = String.valueOf(contactNameEditText.getText());
        contactManager.addContact(name, contactPublicKey);

        Realm realm = Realm.getDefaultInstance();
        Contact contact = realm.where(Contact.class).equalTo("name", name).findFirst();
        conversationManager.addConversation(contact);
        realm.close();

        Toast.makeText(this, contactPublicKey, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }
}
