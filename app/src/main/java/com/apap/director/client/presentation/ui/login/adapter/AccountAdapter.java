package com.apap.director.client.presentation.ui.login.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apap.director.client.R;
import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.presentation.ui.login.adapter.holder.AccountViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Adam Poterałowicz
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountViewHolder> {

    @BindView(R.id.accountListItem_textView)
    TextView accountName;

    private Context context;
    private List<AccountEntity> accountList;

    public AccountAdapter(Context context) {
        super();

        this.context = context;
        this.accountList = new ArrayList<>();
    }


    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.account_list_item, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        final AccountEntity accountEntity = accountList.get(position);
        holder.accountName.setText(accountEntity.getName());
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public void update(List<AccountEntity> accountList) {
        this.accountList = accountList;
        notifyDataSetChanged();
    }

    public void clear() {
        this.accountList.clear();
        notifyDataSetChanged();
    }
}
