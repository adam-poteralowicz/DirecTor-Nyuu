package com.apap.director.client.fragment;

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
import com.apap.director.client.activity.AddContactActivity;
import com.apap.director.client.activity.NewContactActivity;
import com.apap.director.client.activity.SingleContactActivity;
import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.util.ArrayAdapterChangeListener;
import com.apap.director.manager.AccountManager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmResults;


public class ContactsFragment extends Fragment {

    private ArrayList<Contact> contactList;
    private ArrayAdapter<Contact> arrayAdapter;
    private Realm realm;
    private ArrayAdapterChangeListener<Contact, RealmResults<Contact>> changeListener;
    private RealmResults<Contact> allContacts;
    @BindView(R.id.contactsView) ListView contactsListView;

    @Inject
    AccountManager accountManager;


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

        contactList = new ArrayList<Contact>(allContacts);
        arrayAdapter = new ArrayAdapter<Contact>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                contactList);
        contactsListView.setAdapter(arrayAdapter);

        changeListener = new ArrayAdapterChangeListener<Contact, RealmResults<Contact>>(arrayAdapter, "contacts fragment listner");
        allContacts.addChangeListener(changeListener);
    }

    @Override
    public void onDestroy() {
        allContacts.removeChangeListener(changeListener);
        super.onDestroy();
    }


    @OnItemClick(R.id.contactsView)
    public void showContactDetails(int position){
        Intent intent = new Intent(App.getContext(), SingleContactActivity.class);
        intent.putExtra("contactName", contactList.get(position).getName());
        intent.putExtra("contactId", contactList.get(position).getId());
        startActivity(intent);
    }


    @OnClick(R.id.addNewContactButton)
    public void onClick(View view) {
            Intent selectedIntent = new Intent(getActivity(), AddContactActivity.class);
            startActivity(selectedIntent);
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
}
