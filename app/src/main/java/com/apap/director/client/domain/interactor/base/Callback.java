package com.apap.director.client.domain.interactor.base;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public interface Callback<O> {

    void onAccept(O data);
    void onError(Throwable throwable);

}
