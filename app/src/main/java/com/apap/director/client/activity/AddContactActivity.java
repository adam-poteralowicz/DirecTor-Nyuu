package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.manager.DatabaseManager;
import com.apap.director.client.manager.IDatabaseManager;
import com.apap.director.im.dao.model.Contact;

public class AddContactActivity extends Activity {
    private IDatabaseManager databaseManager;
    EditText newContactName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_view);

        newContactName = (EditText) findViewById(R.id.newContactName);
        newContactName.setHint("CONTACT NAME");

        ((App) getApplication()).getDaoComponent().inject(this);

        // init database manager
        databaseManager = new DatabaseManager(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.addContactButton) {
            if (String.valueOf(newContactName.getText()).matches(".*\\w.*")
                    && (String.valueOf(newContactName.getText().charAt(0))).trim().length() > 0) {
                Contact contact = new Contact();
                contact.setName(String.valueOf(newContactName.getText()));
                databaseManager.insertContact(contact);

                Intent selectedIntent = new Intent(AddContactActivity.this, AuthUserActivity.class);
                startActivityForResult(selectedIntent, 0013);
            }
        }
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
