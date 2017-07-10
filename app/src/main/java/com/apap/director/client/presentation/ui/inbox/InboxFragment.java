package com.apap.director.client.presentation.ui.inbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.domain.repository.ConversationRepository;
import com.apap.director.client.presentation.ui.inbox.contract.InboxContract;
import com.apap.director.client.presentation.ui.inbox.di.DaggerInboxComponent;
import com.apap.director.client.presentation.ui.inbox.di.module.InboxContractModule;
import com.apap.director.client.presentation.ui.inbox.presenter.InboxPresenter;
import com.apap.director.client.presentation.ui.message.NewMsgActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class InboxFragment extends Fragment implements InboxContract.View {

    @Inject
    InboxPresenter inboxPresenter;

    @BindView(R.id.msgList)
    ListView msgListView;

    private ArrayList<ConversationEntity> conversationList;
    private ArrayAdapter<ConversationEntity> arrayAdapter;
    private Realm realm;
    private RealmResults<ConversationEntity> conversationResults;
    private RealmResults<ConversationEntity> conversations;
    private ConversationRepository conversationRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inbox_view, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpInjection();
        realm = Realm.getDefaultInstance();

        inboxPresenter.getConversations(null);
        addNewConversations();
        setUpAdapter();
        inboxPresenter.getConversations(conversations);
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

    @Override
    public void refreshConversationList(List<ConversationEntity> data) {
        conversationResults = (RealmResults<ConversationEntity>) data;
    }

    @Override
    public void addChangeListener(List<ConversationEntity> data) {
        conversations = (RealmResults<ConversationEntity>) data;
        conversations.addChangeListener(results -> {
            conversations.size();
        });
    }

    @Override
    public void handleSuccess(String message) {
        Log.v(getClass().getSimpleName(), message);
    }

    @Override
    public void handleException(Throwable throwable) {
        Log.getStackTraceString(throwable);
    }

    @OnItemClick(R.id.msgList)
    public void startSendMessageActivity(int position) {
        Snackbar.make(getView(), conversationList.get(position).getContact().getName(), Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), NewMsgActivity.class)
                .putExtra("contactId", conversationList.get(position).getContact().getId())
                .putExtra("msgTitle", conversationList.get(position).getContact().getName());
        startActivity(intent);
    }

    @OnItemLongClick(R.id.msgList)
    public boolean deleteConversation(int position) {
        inboxPresenter.deleteConversation(conversationList.get(position));
        conversationList.remove(position);
        arrayAdapter.notifyDataSetChanged();
        return true;
    }

    private void setUpInjection() {
        DaggerInboxComponent.builder()
                .mainComponent(((App) getActivity().getApplication()).getComponent())
                .inboxContractModule(new InboxContractModule((InboxContract.View) getView(), conversationRepository))
                .build()
                .inject(this);
    }

    private void setUpAdapter() {
        arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                conversationList);
        msgListView.setAdapter(arrayAdapter);
    }

    private void addNewConversations() {
        conversationList = new ArrayList<>();
        if (!conversationResults.isEmpty())
            conversationList.addAll(realm.copyFromRealm(conversationResults));
    }
}
