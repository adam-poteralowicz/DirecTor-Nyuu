package com.apap.director.client.domain.interactor.base;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.NonNull;

public abstract class BaseInteractor<D, P> {

    public abstract Observable<D>  buildObservable(P p);

    public Observable<D> execute(P p) {
        return buildObservable(p)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
