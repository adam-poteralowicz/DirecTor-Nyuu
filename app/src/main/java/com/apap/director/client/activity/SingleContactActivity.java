package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.manager.AccountManager;
import com.apap.director.manager.ContactManager;
import com.apap.director.client.R;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.manager.ConversationManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SingleContactActivity extends Activity {

    List<String> myOptionsList = null;

    @BindView(R.id.contactName) TextView contactNameView;
    @BindView(R.id.optionsList) ListView options;
    @BindView(R.id.imageView) ImageView imageView;

    Intent intent;
    String contactNameFromIntent;
    Long contactIdFromIntent;
    EditText contactNameEditText;

    @Inject
    Realm realm;

    @Inject
    ContactManager contactManager;

    @Inject
    AccountManager accountManager;

    @Inject
    ConversationManager conversationManager;

    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.single_contact_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        super.onCreate(savedInstanceState);

        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);
        contactNameEditText = (EditText) findViewById(R.id.contactName);
        imageView = (ImageView) findViewById(R.id.imageView);
        contactNameFromIntent = getIntent().getStringExtra("contactName");
        contactNameView = (TextView) findViewById(R.id.contactName);
        contactNameView.setText(contactNameFromIntent);
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
                        Log.d("DTOR/=/", contactIdFromIntent.toString());
                        if (conversationManager == null) Log.d("CONVMAN", "NULL");
                        Conversation conv = conversationManager.getConversationByContactId(contactIdFromIntent);
                        if (conv == null) {
                            realm.beginTransaction();
                                Conversation conversation = realm.createObject(Conversation.class, conversationManager.generateConversationId());
                                conversation.setContact(realm.where(Contact.class).equalTo("id", contactIdFromIntent).findFirst());
                                conversation.setAccount(accountManager.getActiveAccount());
                                //TODO - conversation.setSessions();
                                realm.copyToRealmOrUpdate(conversation);
                            realm.commitTransaction();
                        }

                        intent = new Intent(App.getContext(), NewMsgActivity.class);
                        intent.putExtra("recipient", contactNameFromIntent);
                        intent.putExtra("contactId", contactIdFromIntent);
                        startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        contactManager.deleteContact(contactNameFromIntent);
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

        contactNameEditText.setSelectAllOnFocus(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {

            Intent selectedIntent = new Intent(SingleContactActivity.this, AuthUserActivity.class);
            startActivity(selectedIntent);

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
            contactManager.updateContact(contactNameFromIntent, imagePath, null, null);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void checkIfAvatarExists() {
        if (realm.where(Contact.class).equalTo("id", contactIdFromIntent).findFirst().getImage() != null) {
            String imagePath = realm.where(Contact.class).equalTo("id", contactIdFromIntent).findFirst().getImage();
            Log.v("Image path: ", imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            imageView.setImageBitmap(bitmap);
        }
    }
}
