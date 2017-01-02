package com.apap.director.db.realm.util;

import android.util.Log;
import android.widget.ArrayAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;


public class ArrayAdapterChangeListener<E extends RealmModel, T extends RealmResults<E>> implements RealmChangeListener<RealmResults<E>> {


    private ArrayAdapter<E> arrayAdapter;

    public ArrayAdapterChangeListener(ArrayAdapter<E> arrayAdapter) {
        this.arrayAdapter = arrayAdapter;
    }
    @Override
    public void onChange(RealmResults elements) {

        Log.v("HAI/ChangeListener", "Change detected");

        arrayAdapter.clear();
        arrayAdapter.addAll(elements);
        arrayAdapter.notifyDataSetChanged();

    }
}
