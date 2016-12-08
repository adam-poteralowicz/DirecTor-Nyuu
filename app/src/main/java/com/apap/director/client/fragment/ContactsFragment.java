package com.apap.director.client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.activity.AuthUserActivity;
import com.apap.director.client.activity.SingleContactActivity;
import com.apap.director.director_db.manager.DatabaseManager;
import com.apap.director.director_db.manager.IDatabaseManager;
import com.apap.director.director_db.dao.model.Contact;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    public AuthUserActivity aua;
    private IDatabaseManager databaseManager;
    private ArrayList<Contact> contactList;
    private ArrayAdapter<Contact> arrayAdapter;
    private ListView contactsListView;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.contacts_view, container, false);
        aua = (AuthUserActivity) getActivity();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

//        ((App) getActivity().getApplication()).getDaoComponent().inject(this);
        super.onActivityCreated(savedInstanceState);
        contactsListView = (ListView) getActivity().findViewById(R.id.contactsView);

        // init database manager
        databaseManager = new DatabaseManager(getActivity());

        contactList = new ArrayList<Contact>();
        arrayAdapter = new ArrayAdapter<Contact>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                contactList);
        contactsListView.setAdapter(arrayAdapter);

        intent = new Intent(App.getContext(), SingleContactActivity.class);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("contactName", contactList.get(position).getName());
                startActivity(intent);
            }
        });

        refreshContactList();
    }

    /**
     * Display all the users from the DB into the listView
     */
    private void refreshContactList() {
        contactList = DatabaseManager.getInstance(getActivity()).listContacts();
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


}
