package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.dao.model.Contact;
import com.apap.director.client.dao.model.ContactDao;
import com.apap.director.client.dao.model.DaoSession;

public class AddContactActivity extends Activity {
    EditText newContactName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_view);

        newContactName = (EditText) findViewById(R.id.newContactName);
        newContactName.setHint("CONTACT NAME");
    }

    public void onClick(View view) {
        if (view.getId() == R.id.addContactButton) {
            if (String.valueOf(newContactName.getText()).matches(".*\\w.*")
                    && (String.valueOf(newContactName.getText().charAt(0))).trim().length() > 0) {
                DaoSession daoSession = ((App) getApplicationContext()).getContactDaoSession();
                ContactDao contactDao = daoSession.getContactDao();
                Contact contact = new Contact();
                contact.setName(String.valueOf(newContactName.getText()));
                contactDao.insertOrReplace(contact);

                Intent selectedIntent = new Intent(AddContactActivity.this, AuthUserActivity.class);
                startActivityForResult(selectedIntent, 0013);
            }
        }
    }

}
