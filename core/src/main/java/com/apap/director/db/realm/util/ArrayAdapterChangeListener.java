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
    private String name;

    public ArrayAdapterChangeListener(ArrayAdapter<E> arrayAdapter, String name) {
        this.arrayAdapter = arrayAdapter;
        this.name = name;
    }
    @Override
    public void onChange(RealmResults elements) {


       Log.v("HAI/ChangeListener", name+ ": Change detected");

        arrayAdapter.clear();
        arrayAdapter.addAll(elements);
        arrayAdapter.notifyDataSetChanged();

    }
}
