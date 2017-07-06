package com.apap.director.client.presentation.ui.register.presenter;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.interactor.base.Callback;
import com.apap.director.client.domain.interactor.register.CreateAccountInteractor;
import com.apap.director.client.domain.interactor.register.RegisterAccountInteractor;
import com.apap.director.client.domain.interactor.register.SaveAccountInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.presentation.ui.register.contract.RegisterContract;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import rx.subscriptions.CompositeSubscription;

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;
    private RegisterAccountInteractor registerAccountInteractor;
    private CreateAccountInteractor createAccountInteractor;
    private SaveAccountInteractor saveAccountInteractor;
    private CompositeDisposable subscriptions;

    @Inject
    public RegisterPresenter(RegisterContract.View view, RegisterAccountInteractor registerAccountInteractor, CreateAccountInteractor createAccountInteractor, SaveAccountInteractor saveAccountInteractor) {
        this.view = view;
        this.registerAccountInteractor = registerAccountInteractor;
        this.createAccountInteractor = createAccountInteractor;
        this.saveAccountInteractor = saveAccountInteractor;
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    private void registerAccount(AccountModel account) {
        Log.v(RegisterPresenter.class.getSimpleName(), "Sign up request");

        subscriptions.add(registerAccountInteractor.execute(account).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(@NonNull ResponseBody responseBody) throws Exception {
                view.handleSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                view.handleException(throwable);
            }
        }));
    }

    @Override
    public void signUp(String name) {

    }

}
