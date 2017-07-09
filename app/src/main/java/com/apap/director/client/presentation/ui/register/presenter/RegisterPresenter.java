package com.apap.director.client.presentation.ui.register.presenter;

import com.apap.director.client.domain.interactor.account.CreateAccountInteractor;
import com.apap.director.client.domain.interactor.register.RegisterAccountInteractor;
import com.apap.director.client.domain.interactor.register.SaveAccountInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.presentation.ui.register.contract.RegisterContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

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

        subscriptions = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void signUp(String name) {
        subscriptions.add(createAccountInteractor.execute(name)
                .flatMap(this::buildChain)
                .subscribe(accountModel -> view.handleSuccess(),
                        throwable -> view.handleException(throwable)));
    }

    private Observable<AccountModel> buildChain(AccountModel accountModel) {
        return registerAccountInteractor.execute(accountModel)
                .flatMap(responseBody -> saveAccountInteractor.execute(accountModel));
    }
}
