package com.apap.director.client.data.db.mapper.base;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Alicja Michniewicz
 */

public abstract class BaseMapper<M, E extends RealmObject> {

    public abstract E mapToEntity(M model);
    public abstract M mapToModel(E entity);

    public <M2, E2 extends RealmObject> RealmList<E2> mapToRealmList(BaseMapper<M2, E2> mapper, List<M2> models) {
        RealmList<E2> realmList = new RealmList<>();

        for(M2 model : models) {
            realmList.add(mapper.mapToEntity(model));
        }

        return realmList;
    }
    public <M2, E2 extends RealmObject> List<M2> mapToList(BaseMapper<M2, E2> mapper, RealmList<E2> entities) {
        List<M2> list = new ArrayList<>();

        for(E2 entity : entities) {
            list.add(mapper.mapToModel(entity));
        }

        return list;
    }
}
