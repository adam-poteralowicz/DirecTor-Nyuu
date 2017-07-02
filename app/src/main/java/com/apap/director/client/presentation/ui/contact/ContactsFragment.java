package com.apap.director.client.presentation.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.domain.model.Account;
import com.apap.director.client.domain.model.Contact;
import com.apap.director.client.presentation.ui.listener.ArrayAdapterChangeListener;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class ContactsFragment extends Fragment {

    @Inject
    AccountManager accountManager;

    @BindView(R.id.contactsView)
    ListView contactsListView;

    private ArrayList<Contact> contactList;
    private Realm realm;
    private ArrayAdapterChangeListener<Contact, RealmResults<Contact>> changeListener;
    private RealmResults<Contact> allContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_view, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        ((App) getActivity().getApplication()).getComponent().inject(this);
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();

        Account active = realm.where(Account.class).equalTo("active", true).findFirst();

        allContacts = realm.where(Contact.class).equalTo("account.id", active.getId()).findAll();

        contactList = new ArrayList<>(allContacts);
        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                contactList);
        contactsListView.setAdapter(arrayAdapter);

        changeListener = new ArrayAdapterChangeListener<>(arrayAdapter, "contacts fragment listner");
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
}
