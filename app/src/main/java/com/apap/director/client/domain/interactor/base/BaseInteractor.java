package com.apap.director.client.domain.interactor.base;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseInteractor<D, P> {

    protected abstract Observable<D>  buildObservable(P p);

    public Observable<D> execute(P p) {
        return buildObservable(p)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
