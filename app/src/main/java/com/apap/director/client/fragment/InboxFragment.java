package com.apap.director.client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.activity.NewMsgActivity;
import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.db.manager.IDatabaseManager;
import com.apap.director.db.dao.model.Conversation;
import com.apap.director.db.realm.model.Message;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class InboxFragment extends Fragment {

    @Inject DatabaseManager databaseManager;
    private ArrayList<Conversation> conversationList;
    private ArrayAdapter<Conversation> arrayAdapter;
    @BindView(R.id.msgList) ListView msgListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.inbox_view, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getDaoComponent().inject(this);
        super.onActivityCreated(savedInstanceState);

        conversationList = databaseManager.listConversations();
        arrayAdapter = new ArrayAdapter<Conversation>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                conversationList);

        if(arrayAdapter == null) Log.i("ARRAY ADAPTER", "null");

        msgListView.setAdapter(arrayAdapter);

        Realm.init(this.getContext());
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<com.apap.director.db.realm.model.Conversation> conversations = realm.where(com.apap.director.db.realm.model.Conversation.class).findAll();
        conversations.addChangeListener(new RealmChangeListener<RealmResults<com.apap.director.db.realm.model.Conversation>>() {
            @Override
            public void onChange(RealmResults<com.apap.director.db.realm.model.Conversation> results) {
                conversations.size();
            }
        });

    }

    @OnItemClick(R.id.msgList)
    public void startSendMessageActivity(int position){
        Toast.makeText(getActivity(), conversationList.get(position).getRecipient(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), NewMsgActivity.class);
        intent.putExtra("msgTitle", conversationList.get(position).getRecipient());
        startActivity(intent);
    }

    @OnItemLongClick(R.id.msgList)
    public boolean deleteConversation(int position){
        //TODO: Lepiej kasowac konwersacje po id
        conversationList.get(position).getId();
        databaseManager.deleteConversationByRecipient(conversationList.get(position).getRecipient());
        conversationList.remove(position);
        arrayAdapter.notifyDataSetChanged();
        return true;
    }
}
