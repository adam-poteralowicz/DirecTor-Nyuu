package com.apap.director.client.presentation.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.presentation.ui.contact.contract.ContactsContract;
import com.apap.director.client.presentation.ui.contact.di.component.DaggerContactsComponent;
import com.apap.director.client.presentation.ui.contact.di.module.ContactsContractModule;
import com.apap.director.client.presentation.ui.contact.presenter.ContactsPresenter;
import com.apap.director.client.presentation.ui.listener.ArrayAdapterChangeListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class ContactsFragment extends Fragment implements ContactsContract.View {

    @Inject
    ContactsPresenter contactsPresenter;

    @BindView(R.id.contactsView)
    ListView contactsListView;

    private ArrayList<ContactEntity> contactList;
    private Realm realm;
    private ArrayAdapterChangeListener<ContactEntity, RealmResults<ContactEntity>> changeListener;
    private RealmResults<ContactEntity> allContacts;
    private AccountEntity active;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_view, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setUpInjection();
        realm = Realm.getDefaultInstance();

        contactsPresenter.getActiveAccount();
        contactsPresenter.getContactList();

        changeListener = new ArrayAdapterChangeListener<>(setUpAdapter(), "contacts fragment listener");
        allContacts.addChangeListener(changeListener);
    }

    @Override
    public void onDestroy() {
        allContacts.removeChangeListener(changeListener);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        allContacts.addChangeListener(changeListener);
    }

    @Override
    public void onStop() {
        allContacts.removeChangeListener(changeListener);
        super.onStop();
        realm.close();
    }

    @Override
    public void retrieveActiveAccount(AccountEntity account) {
        active = account;
    }

    @Override
    public void refreshContactList(List<ContactEntity> data) {
        allContacts = (RealmResults<ContactEntity>) data;
    }

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
    }

    @OnItemClick(R.id.contactsView)
    public void showContactDetails(int position) {
        Intent intent = new Intent(App.getContext(), SingleContactActivity.class)
                .putExtra("contactName", contactList.get(position).getName())
                .putExtra("contactId", contactList.get(position).getId());
        startActivity(intent);
    }

    @OnClick(R.id.addNewContactButton)
    public void onClick() {
        Intent selectedIntent = new Intent(getActivity(), AddContactActivity.class);
        startActivity(selectedIntent);
    }

    private void setUpInjection() {
        DaggerContactsComponent.builder()
                .mainComponent(((App) getActivity().getApplication()).getComponent())
                .contactsContractModule(new ContactsContractModule(this, contactsPresenter))
                .build()
                .inject(this);
    }

    private ArrayAdapter<ContactEntity> setUpAdapter() {
        contactList = new ArrayList<>(allContacts);
        ArrayAdapter<ContactEntity> arrayAdapter = new ArrayAdapter<>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                contactList);
        contactsListView.setAdapter(arrayAdapter);

        return arrayAdapter;
    }
}
