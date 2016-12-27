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
import com.apap.director.client.activity.SingleContactActivity;
import com.apap.director.db.dao.model.Contact;
import com.apap.director.db.manager.DatabaseManager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class ContactsFragment extends Fragment {

    @Inject DatabaseManager databaseManager;

    private ArrayList<Contact> contactList;
    private ArrayAdapter<Contact> arrayAdapter;
    @BindView(R.id.contactsView) ListView contactsListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_view, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        ((App) getActivity().getApplication()).getDaoComponent().inject(this);
        super.onActivityCreated(savedInstanceState);

        contactList = new ArrayList<Contact>();
        arrayAdapter = new ArrayAdapter<Contact>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                contactList);
        contactsListView.setAdapter(arrayAdapter);

        refreshContactList();
    }

    @OnItemClick(R.id.contactsView)
    public void showContactDetails(int position){
        Intent intent = new Intent(App.getContext(), SingleContactActivity.class);
        intent.putExtra("contactName", contactList.get(position).getName());
        intent.putExtra("contactId", contactList.get(position).getId());
        startActivity(intent);
    }

    /**
     * Display all the users from the DB into the listView
     */
    private void refreshContactList() {
        contactList = databaseManager.listContacts();
        if (contactList != null) {
            if (arrayAdapter == null) {
                arrayAdapter = new ArrayAdapter<Contact>(
                        App.getContext(),
                        android.R.layout.simple_list_item_1,
                        contactList);
                contactsListView.setAdapter(arrayAdapter);
                if (contactList.isEmpty())
                    ;
            } else {
                contactsListView.setAdapter(null);
                arrayAdapter.clear();
                arrayAdapter.addAll(contactList);
                arrayAdapter.notifyDataSetChanged();
                contactsListView.setAdapter(arrayAdapter);
            }
        }
    }

    @OnClick(R.id.addNewContactButton)
    public void onClick(View view) {
            Intent selectedIntent = new Intent(getActivity(), AddContactActivity.class);
            startActivityForResult(selectedIntent, 0010);
    }

}
