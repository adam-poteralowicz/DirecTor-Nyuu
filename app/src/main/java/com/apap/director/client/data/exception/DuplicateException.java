package com.apap.director.client.data.exception;

/**
 * Created by Alicja Michniewicz
 */

public class DuplicateException extends Exception {

    public DuplicateException(String entityName, String field, String fieldValue) {
        super(String.format("%s with %s %s already exists", entityName, field, fieldValue));
    }
}
