package com.apap.director.client.presentation.ui.contact;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.service.DbContactService;
import com.apap.director.client.data.db.service.DbConversationService;
import com.apap.director.client.presentation.ui.contact.contract.SingleContactContract;
import com.apap.director.client.presentation.ui.contact.di.component.DaggerSingleContactComponent;
import com.apap.director.client.presentation.ui.contact.di.module.SingleContactContractModule;
import com.apap.director.client.presentation.ui.contact.presenter.SingleContactPresenter;
import com.apap.director.client.presentation.ui.home.HomeActivity;
import com.apap.director.client.presentation.ui.message.NewMsgActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class SingleContactActivity extends Activity implements SingleContactContract.View {

    @Inject
    SingleContactPresenter singleContactPresenter;
    @Inject
    Realm realm;

    @BindView(R.id.contactName)
    EditText contactName;
    @BindView(R.id.optionsList)
    ListView optionsListView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.singleContactActivity_rootView)
    View rootView;

    private String TAG = App.getContext().getClass().getSimpleName();
    List<String> myOptionsList = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    Intent intent;
    String contactNameFromIntent;
    Long contactIdFromIntent;
    DbContactService dbContactService;
    DbConversationService dbConversationService;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.single_contact_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpInjection();
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        contactIdFromIntent = getIntent().getLongExtra("contactId", 1L);
        contactNameFromIntent = getIntent().getStringExtra("contactName");
        contactName.setText(contactNameFromIntent);

        singleContactPresenter.checkAvatar(options, contactIdFromIntent);
        singleContactPresenter.initOptions(myOptionsList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                myOptionsList);
        optionsListView.setAdapter(arrayAdapter);

        optionsListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Snackbar.make(rootView, myOptionsList.get(position), Snackbar.LENGTH_LONG).show();

            switch (position) {
                case 0:
                    Log.d(TAG, contactIdFromIntent.toString());

                    ConversationEntity conv = dbConversationService.getConversationByContactId(contactIdFromIntent);
                    if (conv == null) {
                        conv = singleContactPresenter.createConversation(dbContactService.getContactByName(contactNameFromIntent));
                        singleContactPresenter.decorateConversation(conv, contactIdFromIntent);
                    }

                    intent = new Intent(App.getContext(), NewMsgActivity.class)
                            .putExtra("recipient", contactNameFromIntent)
                            .putExtra("contactId", contactIdFromIntent);
                    startActivity(intent);
                    break;
                case 1:
                    singleContactPresenter.deleteContact(dbContactService.getContactByName(contactNameFromIntent));
                    startActivity(new Intent(App.getContext(), HomeActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(App.getContext(), HomeActivity.class));
                    break;
                default:
                    break;
            }
        });

        contactName.setSelectAllOnFocus(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(data.getData(), filePath, null, null, null);

            singleContactPresenter.getAvatar(cursor, filePath, contactNameFromIntent, options);
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

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
    }

    @Override
    public void showAvatar(String imagePath, BitmapFactory.Options options) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));
    }

    @Override
    public void setConversationContact(ConversationEntity conversation, ContactEntity contact) {
        realm.beginTransaction();
        conversation.setContact(contact);
        realm.copyToRealmOrUpdate(conversation);
        realm.commitTransaction();
    }

    @Override
    public void handleSuccess(String message) {
        Log.v(TAG, message);
    }

    @OnClick(R.id.imageView)
    public void uploadAvatar(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    private void setUpInjection() {
        DaggerSingleContactComponent.builder()
                .mainComponent(((App) getApplication()).getComponent())
                .singleContactContractModule(new SingleContactContractModule(this))
                .build()
                .inject(this);
    }
}
