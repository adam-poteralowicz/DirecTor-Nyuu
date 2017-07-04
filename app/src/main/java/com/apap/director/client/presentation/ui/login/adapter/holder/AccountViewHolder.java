package com.apap.director.client.presentation.ui.login.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.apap.director.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adam on 2017-07-04.
 */

public class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.accountListItem_textView)
    public TextView accountName;

    public AccountViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this.itemView);

        itemView.setClickable(true);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
