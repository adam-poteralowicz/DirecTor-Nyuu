package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.director_db.manager.DatabaseManager;
import com.apap.director.director_db.manager.IDatabaseManager;
import com.apap.director.director_db.dao.model.Contact;
import com.apap.director.director_db.dao.model.Conversation;

import java.util.ArrayList;
import java.util.List;

public class SingleContactActivity extends Activity {

    private IDatabaseManager databaseManager;
    List<String> myOptionsList = null;
    TextView contactNameView;
    ListView options;
    ImageView imageView;
    Intent intent;
    String contactNameFromIntent;

    public void onCreate(Bundle savedInstanceState) {
//        ((App) getApplication()).getDaoComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_contact_view);

        // init database manager
        databaseManager = new DatabaseManager(this);

        imageView = (ImageView) findViewById(R.id.imageView);
        contactNameFromIntent = getIntent().getStringExtra("contactName");
        contactNameView = (TextView) findViewById(R.id.contactName);
        contactNameView.setText(contactNameFromIntent);
        options = (ListView) findViewById(R.id.optionsList);
        myOptionsList = new ArrayList<String>();
        myOptionsList.add("Send message");
        myOptionsList.add("Delete from contacts");
        myOptionsList.add("Return");

        checkIfAvatarExists();

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
                            conversation.setContactId(contactId);
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


    public void uploadAvatar(View view) {
        if (view.getId() == R.id.imageView) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();

            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            Contact contact = databaseManager.getContactByName(contactNameFromIntent);
            contact.setImage(imagePath);
            databaseManager.updateContact(contact);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void checkIfAvatarExists() {
        if (databaseManager.getContactByName(contactNameFromIntent).getImage() != null) {
            String imagePath = databaseManager.getContactByName(contactNameFromIntent).getImage();
            Log.v("Image path: ", imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            imageView.setImageBitmap(bitmap);
        }
    }
}
