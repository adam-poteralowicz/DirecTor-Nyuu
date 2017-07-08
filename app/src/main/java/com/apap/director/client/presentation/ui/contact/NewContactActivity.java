package com.apap.director.client.presentation.ui.contact;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.service.DbContactService;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.presentation.ui.contact.contract.NewContactContract;
import com.apap.director.client.presentation.ui.contact.di.component.DaggerNewContactComponent;
import com.apap.director.client.presentation.ui.contact.di.module.NewContactContractModule;
import com.apap.director.client.presentation.ui.contact.presenter.NewContactPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewContactActivity extends Activity implements NewContactContract.View {

    @Inject
    NewContactPresenter newContactPresenter;
    @Inject
    ConversationManager conversationManager;

    @BindView(R.id.contactNameEditText)
    EditText contactNameEditText;
    @BindView(R.id.theirPublicKey)
    TextView textView;
    @BindView(R.id.newContactActivity_rootView)
    View rootView;

    private String contactPublicKey;
    private DbContactService dbContactService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contact_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpInjection();
        ButterKnife.bind(this);

        contactPublicKey = getIntent().getExtras().getString("key");
        textView.setText("ContactEntity public key: " + contactPublicKey);
    }

    @OnClick(R.id.saveContactButton)
    public void addContact() {

        String contactName = String.valueOf(contactNameEditText.getText());
        newContactPresenter.addContact(contactName, contactPublicKey);
        newContactPresenter.addConversation(contactName);

        Snackbar.make(rootView, contactPublicKey, Snackbar.LENGTH_LONG).show();

        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }

    @Override
    public void showSnackbar(String text) {
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void addConversation(String contactName) {
        conversationManager.addConversation(dbContactService.getContactByName(contactName));
    }

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
    }

    private void setUpInjection() {
        DaggerNewContactComponent.builder()
                .mainComponent(((App) getApplication()).getComponent())
                .newContactContractModule(new NewContactContractModule(this))
                .build()
                .inject(this);
    }
}
