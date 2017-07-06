package com.apap.director.client.domain.interactor.base;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.NonNull;

public abstract class BaseInteractor<D, P> {

    private Disposable disposable;
    
    public abstract Observable<D>  buildObservable(P p);

    public void execute(P p, @NonNull final Callback<D> callback) {
        disposable = buildObservable(p)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<D>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull D d) throws Exception {
                        callback.onAccept(d);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        callback.onError(throwable);
                    }
                });
    }

    public void dispose() {
        disposable.dispose();
    }

}
