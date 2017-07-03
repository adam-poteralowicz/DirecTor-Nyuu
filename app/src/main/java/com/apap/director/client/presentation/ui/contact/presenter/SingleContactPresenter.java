package com.apap.director.client.presentation.ui.contact.presenter;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.service.DbContactService;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.SingleContactContract;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Adam on 2017-07-03.
 */

public class SingleContactPresenter implements BasePresenter, SingleContactContract.Presenter {

    @Inject
    Realm realm;
    @Inject
    ContactManager contactManager;
    @Inject
    AccountManager accountManager;

    private SingleContactContract.View view;
    private DbContactService dbContactService;

    @Inject
    SingleContactPresenter(SingleContactContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void checkAvatar(BitmapFactory.Options options, Long contactId) {
        if (realm.where(ContactEntity.class).equalTo("id", contactId).findFirst().getImage() != null) {
            String imagePath = realm.where(ContactEntity.class).equalTo("id", contactId).findFirst().getImage();
            Log.v("Image path: ", imagePath);

            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            view.showAvatar(imagePath, options);
        }
    }

    @Override
    public void initOptions(List<String> options) {
        options.add("Send message");
        options.add("Delete from contacts");
        options.add("Return");
    }

    @Override
    public void getAvatar(Cursor cursor, String[] filePath, String contactName, BitmapFactory.Options options) {
        assert cursor != null;
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        contactManager.updateContact(contactName, imagePath, null, null);
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        cursor.close();

        view.showAvatar(imagePath, options);
    }

    @Override
    public void decorateConversation(ConversationEntity conversation, Long contactId) {
        view.setConversationAccount(conversation, accountManager.getActiveAccount());
        view.setConversationContact(conversation, dbContactService.getContactById(contactId));
    }
}
