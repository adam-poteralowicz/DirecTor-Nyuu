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
import com.apap.director.client.dao.model.Contact;
import com.apap.director.client.dao.model.ContactDao;
import com.apap.director.client.dao.model.DaoSession;

import java.util.List;

public class ContactsFragment extends Fragment {

    public AuthUserActivity aua;
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

        super.onActivityCreated(savedInstanceState);
        ListView contactsListView = (ListView) getActivity().findViewById(R.id.contactsView);

        DaoSession daoSession = ((App) App.getContext()).getContactDaoSession();
        ContactDao contactDao = daoSession.getContactDao();
        final List<Contact> contactsList = contactDao.loadAll();

        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<Contact>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                contactsList);
        contactsListView.setAdapter(arrayAdapter);

        intent = new Intent(App.getContext(), SingleContactActivity.class);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("contactName", contactsList.get(position).getName());
                startActivity(intent);
            }
        });
    }
}
