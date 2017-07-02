package com.apap.director.client.presentation.ui.inbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.domain.model.Conversation;
import com.apap.director.client.presentation.ui.message.NewMsgActivity;


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

    @Inject
    ConversationManager conversationManager;

    @BindView(R.id.msgList)
    ListView msgListView;

    private ArrayList<Conversation> conversationList;
    private ArrayAdapter<Conversation> arrayAdapter;
    private Realm realm;

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

        conversationList = new ArrayList<>();
        RealmResults<Conversation> conversationResults = realm.where(Conversation.class).findAll();
        if (!conversationResults.isEmpty())
            conversationList.addAll(realm.copyFromRealm(conversationResults));
        arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                conversationList);

        msgListView.setAdapter(arrayAdapter);

        final RealmResults<Conversation> conversations = realm.where(Conversation.class).findAll();
        conversations.addChangeListener(new RealmChangeListener<RealmResults<Conversation>>() {
            @Override
            public void onChange(RealmResults<Conversation> results) {
                conversations.size();
            }
        });

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

    @OnItemClick(R.id.msgList)
    public void startSendMessageActivity(int position) {
        Toast.makeText(getActivity(), conversationList.get(position).getContact().getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), NewMsgActivity.class)
                .putExtra("contactId", conversationList.get(position).getContact().getId())
                .putExtra("msgTitle", conversationList.get(position).getContact().getName());
        startActivity(intent);
    }

    @OnItemLongClick(R.id.msgList)
    public boolean deleteConversation(int position) {
        conversationManager.deleteConversationByContactId(conversationList.get(position).getContact().getId());
        conversationList.remove(position);
        arrayAdapter.notifyDataSetChanged();
        return true;
    }
}
