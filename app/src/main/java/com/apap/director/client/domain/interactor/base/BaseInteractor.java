package com.apap.director.client.domain.interactor.base;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.NonNull;

public abstract class BaseInteractor<Data, Parameter> {

    private Disposable disposable;

    public abstract Observable<Data>  buildObservable(Parameter parameter);

    public void execute(@NonNull Consumer<Data> callback, Parameter parameter) {
        disposable = buildObservable(parameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public void dispose() {
        disposable.dispose();
    }
}
