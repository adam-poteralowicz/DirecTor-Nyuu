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
import android.widget.Toast;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.activity.NewMsgActivity;
import com.apap.director.client.dao.model.Conversation;
import com.apap.director.client.dao.model.ConversationDao;
import com.apap.director.client.dao.model.DaoSession;

import java.util.List;

public class InboxFragment extends Fragment {

    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.inbox_view, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView msgListView = (ListView) getActivity().findViewById(R.id.msgList);

        DaoSession daoSession = ((App) App.getContext()).getConversationDaoSession();
        ConversationDao conversationDao = daoSession.getConversationDao();
        final List<Conversation> conversationsList = conversationDao.loadAll();

        final ArrayAdapter<Conversation> arrayAdapter = new ArrayAdapter<Conversation>(
                App.getContext(),
                android.R.layout.simple_list_item_1,
                conversationsList);
        msgListView.setAdapter(arrayAdapter);

        intent = new Intent(App.getContext(), NewMsgActivity.class);

        msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(App.getContext(), conversationsList.get(position).getRecipient(), Toast.LENGTH_LONG).show();
                intent.putExtra("msgTitle", conversationsList.get(position).getRecipient());
                startActivity(intent);
            }
        });

        msgListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DaoSession daoSession = ((App) App.getContext()).getConversationDaoSession();
                ConversationDao conversationDao = daoSession.getConversationDao();
                conversationDao.deleteByKey(conversationsList.get(position).getRecipient());
                arrayAdapter.notifyDataSetChanged();
                onActivityCreated(savedInstanceState);

                return true;
            }
        });

    }
}
