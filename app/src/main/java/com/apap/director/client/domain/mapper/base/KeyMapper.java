package com.apap.director.client.domain.mapper.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alicja Michniewicz
 */

public abstract class KeyMapper<M, R> {

    public abstract M mapRecordToModel(R record, long dbId);
    public abstract R mapModelToRecord(M model) throws IOException;
    public List<M> mapRecordListToModelList(List<R> records, long startId) {
        List<M> models = new ArrayList<>();

        for(R record : records) {
            models.add(mapRecordToModel(record, startId));
            startId = startId + 1;
        }

        return models;
    }

    public List<R> mapModelListToRecordList(List<M> models) throws IOException {
        List<R> records = new ArrayList<>();

        for(M model : models) {
            records.add(mapModelToRecord(model));
        }

        return records;
    }

}
