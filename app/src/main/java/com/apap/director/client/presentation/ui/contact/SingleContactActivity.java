package com.apap.director.client.presentation.ui.contact;

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
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.presentation.ui.home.HomeActivity;
import com.apap.director.client.presentation.ui.message.NewMsgActivity;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SingleContactActivity extends Activity {

    @Inject
    Realm realm;
    @Inject
    ContactManager contactManager;
    @Inject
    AccountManager accountManager;
    @Inject
    ConversationManager conversationManager;

    @BindView(R.id.contactName)
    EditText contactName;
    @BindView(R.id.optionsList)
    ListView optionsListView;
    @BindView(R.id.imageView)
    ImageView imageView;

    private String TAG = App.getContext().getClass().getSimpleName();
    List<String> myOptionsList = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    Intent intent;
    String contactNameFromIntent;
    Long contactIdFromIntent;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.single_contact_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((App) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);
        contactNameFromIntent = getIntent().getStringExtra("contactName");
        contactName.setText(contactNameFromIntent);

        initOptions((ArrayList<String>) myOptionsList);
        checkIfAvatarExists();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                myOptionsList);

        optionsListView.setAdapter(arrayAdapter);
        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(App.getContext(), myOptionsList.get(position), Toast.LENGTH_LONG).show();
                switch (position) {
                    case 0:
                        Log.d(TAG, contactIdFromIntent.toString());
                        if (conversationManager == null) {
                            Log.d("CONVMAN", "NULL");
                        }

                        ConversationEntity conv = conversationManager.getConversationByContactId(contactIdFromIntent);
                        if (conv == null) {
                            realm.beginTransaction();
                            ConversationEntity conversation = realm.createObject(ConversationEntity.class, conversationManager.generateConversationId(realm));
                            conversation.setContact(realm.where(ContactEntity.class).equalTo("id", contactIdFromIntent).findFirst());
                            conversation.setAccount(accountManager.getActiveAccount());
                            //TODO - conversation.setSessions();
                            realm.copyToRealmOrUpdate(conversation);
                            realm.commitTransaction();
                        }

                        intent = new Intent(App.getContext(), NewMsgActivity.class)
                                .putExtra("recipient", contactNameFromIntent)
                                .putExtra("contactId", contactIdFromIntent);
                        startActivity(intent);
                        break;
                    case 1:
                        contactManager.deleteContact(contactNameFromIntent);
                        startActivity(new Intent(App.getContext(), HomeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(App.getContext(), HomeActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

        contactName.setSelectAllOnFocus(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            contactManager.updateContact(contactNameFromIntent, imagePath, null, null);

            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            cursor.close();

            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {

        Intent selectedIntent = new Intent(SingleContactActivity.this, HomeActivity.class);
        startActivity(selectedIntent);

    }

    public void uploadAvatar(View view) {
        if (view.getId() == R.id.imageView) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
    }

    public void checkIfAvatarExists() {
        if (realm.where(ContactEntity.class).equalTo("id", contactIdFromIntent).findFirst().getImage() != null) {
            String imagePath = realm.where(ContactEntity.class).equalTo("id", contactIdFromIntent).findFirst().getImage();
            Log.v("Image path: ", imagePath);

            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));
        }
    }

    private void initOptions(ArrayList<String> options) {
        options.add("Send message");
        options.add("Delete from contacts");
        options.add("Return");
    }
}
