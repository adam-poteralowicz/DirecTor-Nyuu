package com.apap.director.client.data.net.mapper.base;

/**
 * Created by Adam Poterałowicz
 */

public abstract class BaseTOMapper<M, T> {

    public abstract T mapToTO(M model);
    public abstract M mapToModel(T to);
}
