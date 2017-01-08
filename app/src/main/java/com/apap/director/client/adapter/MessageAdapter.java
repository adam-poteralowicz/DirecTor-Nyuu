package com.apap.director.client.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apap.director.client.R;
import com.apap.director.db.realm.model.Message;

import java.util.List;

import butterknife.OnClick;
import io.realm.Realm;


public class MessageAdapter extends ArrayAdapter<Message> {
    private Activity activity;
    private List<Message> messages;
    private Realm realm;

    private final int VIEW_TYPE_MINE = 0;
    private final int VIEW_TYPE_OTHER = 1;

    public MessageAdapter(Activity context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.activity = context;
        realm = Realm.getDefaultInstance();
        this.messages = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Message message = getItem(position);

        if (convertView == null) {
            if(getItemViewType(position) == VIEW_TYPE_MINE) {

                convertView = View.inflate(activity, R.layout.item_chat_right, null);
                holder = new ViewHolder(convertView);
            }
            else{
                convertView = View.inflate(activity,R.layout.item_chat_left, null);
                holder = new ViewHolder(convertView);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.msg.setText(message.getContent());
        return convertView;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getItemViewType(int position)
    {
        return messages.get(position).isMine() == true ?  VIEW_TYPE_MINE : VIEW_TYPE_OTHER;
    }

    private class ViewHolder {
        public TextView msg;

        public ViewHolder(View v) {
            msg = (TextView) v.findViewById(R.id.txt_msg);
        }
    }
}
