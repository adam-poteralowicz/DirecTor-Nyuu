package com.apap.director.client.presentation.ui.contact.presenter;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.mapper.ContactMapper;
import com.apap.director.client.data.db.mapper.ConversationMapper;
import com.apap.director.client.data.db.service.DbContactService;
import com.apap.director.client.domain.interactor.contact.DeleteContactInteractor;
import com.apap.director.client.domain.interactor.contact.UpdateContactInteractor;
import com.apap.director.client.domain.interactor.inbox.CreateConversationInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.contact.contract.SingleContactContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by Adam Poterałowicz.
 */

public class SingleContactPresenter implements BasePresenter, SingleContactContract.Presenter {

    @Inject
    Realm realm;

    private SingleContactContract.View view;
    private DeleteContactInteractor deleteContactInteractor;
    private CreateConversationInteractor createConversationInteractor;
    private UpdateContactInteractor updateContactInteractor;

    private DbContactService dbContactService;
    private ContactMapper contactMapper;
    private ConversationMapper conversationMapper;
    private CompositeDisposable subscriptions;

    @Inject
    SingleContactPresenter(SingleContactContract.View view,
                           DeleteContactInteractor deleteContactInteractor,
                           CreateConversationInteractor createConversationInteractor,
                           UpdateContactInteractor updateContactInteractor) {
        this.view = view;
        this.deleteContactInteractor = deleteContactInteractor;
        this.createConversationInteractor = createConversationInteractor;
        this.updateContactInteractor = updateContactInteractor;

        subscriptions = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
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
        updateContact(contactName, imagePath);
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        cursor.close();

        view.showAvatar(imagePath, options);
    }

    @Override
    public void decorateConversation(ConversationEntity conversation, Long contactId) {
        view.setConversationContact(conversation, dbContactService.getContactById(contactId));
    }

    @Override
    public void deleteContact(ContactEntity contactEntity) {
        subscriptions.add(deleteContactInteractor.execute(contactMapper.mapToModel(contactEntity))
                .subscribe(aBoolean -> view.handleSuccess("Contact " + contactEntity.getName() + " deleted: " + aBoolean),
                        throwable -> view.handleException(throwable)));
    }

    @Override
    public ConversationEntity createConversation(ContactEntity contactEntity) {
        final ConversationModel[] model = { new ConversationModel() };
        subscriptions.add(createConversationInteractor.execute(contactMapper.mapToModel(contactEntity))
                .subscribe(conversationModel -> model[0] = conversationModel,
                        throwable -> view.handleException(throwable)));

        return conversationMapper.mapToEntity(model[0]);
    }

    @Override
    public void updateContact(String contactName, String imagePath) {
        ContactModel contact = contactMapper.mapToModel(dbContactService.getContactByName(contactName));
        contact.setImage(imagePath);

        subscriptions.add(updateContactInteractor.execute(contact)
                .subscribe(contactModel -> view.handleSuccess(contactName + ": avatar retrieved"),
                        throwable -> view.handleException(throwable)));
    }
}
