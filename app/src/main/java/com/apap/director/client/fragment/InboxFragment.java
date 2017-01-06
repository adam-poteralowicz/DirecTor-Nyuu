package com.apap.director.client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.activity.NewMsgActivity;
import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.manager.ConversationManager;

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

    private ArrayList<Conversation> conversationList;
    private ArrayAdapter<Conversation> arrayAdapter;
    private Realm realm;
    @BindView(R.id.msgList) ListView msgListView;

    ConversationManager conversationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inbox_view, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();

        conversationList = new ArrayList<Conversation>();
        ArrayList<Conversation> conversationResults = conversationManager.listAllConversations();
        if (!conversationResults.isEmpty())
            conversationList.addAll(realm.copyFromRealm(conversationResults));
        arrayAdapter = new ArrayAdapter<Conversation>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                conversationList);

        if(arrayAdapter == null) Log.i("ARRAY ADAPTER", "null");

        msgListView.setAdapter(arrayAdapter);

        final RealmResults<Conversation> conversations = realm.where(Conversation.class).findAll();
        conversations.addChangeListener(new RealmChangeListener<RealmResults<com.apap.director.db.realm.model.Conversation>>() {
            @Override
            public void onChange(RealmResults<com.apap.director.db.realm.model.Conversation> results) {
                conversations.size();
            }
        });

    }

    @OnItemClick(R.id.msgList)
    public void startSendMessageActivity(int position){
        Toast.makeText(getActivity(), conversationList.get(position).getContact().getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), NewMsgActivity.class);
        intent.putExtra("contactId", conversationList.get(position).getContact().getId());
        intent.putExtra("msgTitle", conversationList.get(position).getContact().getName());
        startActivity(intent);
    }

    @OnItemLongClick(R.id.msgList)
    public boolean deleteConversation(int position){
        conversationManager.deleteConversationByContactId(conversationList.get(position).getContact().getId());
        conversationList.remove(position);
        arrayAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }
}
