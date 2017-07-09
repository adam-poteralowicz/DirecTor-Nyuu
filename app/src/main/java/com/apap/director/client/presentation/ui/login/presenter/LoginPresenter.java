package com.apap.director.client.presentation.ui.login.presenter;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.mapper.AccountMapper;
import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.domain.interactor.account.CreateAccountInteractor;
import com.apap.director.client.domain.interactor.account.GetActiveAccountInteractor;
import com.apap.director.client.domain.interactor.login.ChooseAccountInteractor;
import com.apap.director.client.domain.interactor.login.DeleteAccountInteractor;
import com.apap.director.client.domain.interactor.login.GetAccountListInteractor;
import com.apap.director.client.domain.interactor.login.LoginInteractor;
import com.apap.director.client.domain.interactor.login.PostOneTimeKeysInteractor;
import com.apap.director.client.domain.interactor.login.PostSignedKeysInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.presentation.ui.login.contract.LoginContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.ResponseBody;


public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private GetAccountListInteractor getAccountListInteractor;
    private GetActiveAccountInteractor getActiveAccountInteractor;
    private CreateAccountInteractor createAccountInteractor;
    private LoginInteractor loginInteractor;
    private PostOneTimeKeysInteractor postOneTimeKeysInteractor;
    private PostSignedKeysInteractor postSignedKeysInteractor;
    private ChooseAccountInteractor chooseAccountInteractor;
    private DeleteAccountInteractor deleteAccountInteractor;

    private AccountMapper accountMapper;
    private AccountStore accountStore;
    private CompositeDisposable subscriptions;

    @Inject
    public LoginPresenter(LoginContract.View view,
                          GetAccountListInteractor getAccountListInteractor,
                          GetActiveAccountInteractor getActiveAccountInteractor,
                          CreateAccountInteractor createAccountInteractor,
                          LoginInteractor loginInteractor,
                          PostOneTimeKeysInteractor postOneTimeKeysInteractor,
                          PostSignedKeysInteractor postSignedKeysInteractor,
                          ChooseAccountInteractor chooseAccountInteractor,
                          DeleteAccountInteractor deleteAccountInteractor) {
        this.view = view;
        this.getAccountListInteractor = getAccountListInteractor;
        this.getActiveAccountInteractor = getActiveAccountInteractor;
        this.createAccountInteractor = createAccountInteractor;
        this.loginInteractor = loginInteractor;
        this.postOneTimeKeysInteractor = postOneTimeKeysInteractor;
        this.postSignedKeysInteractor = postSignedKeysInteractor;
        this.chooseAccountInteractor = chooseAccountInteractor;
        this.deleteAccountInteractor = deleteAccountInteractor;

        subscriptions = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        subscriptions.dispose();
        subscriptions.clear();
    }

    @Override
    public void getAccountList() {
        subscriptions.add(getAccountListInteractor.execute(null)
                .subscribe(data -> {
                            List<AccountEntity> entities = new ArrayList<>();
                            for (int i = 0; i < data.size(); i++) {
                                entities.add(accountMapper.mapToEntity(data.get(i)));
                            }
                            view.refreshAccountList(entities);
                        },
                        throwable -> view.handleException(throwable)));
    }

    @Override
    public AccountEntity getActiveAccount() {
        final AccountEntity[] entity = {new AccountEntity()};
        subscriptions.add(getActiveAccountInteractor.execute(null)
                .subscribe(accountModel -> {
                            entity[0] = accountMapper.mapToEntity(accountModel);
                        },
                        throwable -> view.handleException(throwable)));

        return entity[0];
    }

    @Override
    public void signUp(AccountEntity accountEntity) {
        // to be implemented
    }

    @Override
    public AccountEntity createAccount(String name) {
        final AccountEntity[] entity = {new AccountEntity()};
        subscriptions.add(createAccountInteractor.execute(name)
                .subscribe(accountModel -> {
                            entity[0] = accountMapper.mapToEntity(accountModel);
                        },
                        throwable -> view.handleException(throwable)));

        return entity[0];
    }

    @Override
    public String logIn(AccountModel accountModel) {
        try {
            final ResponseBody[] response = new ResponseBody[0];
            subscriptions.add(loginInteractor.execute(accountModel)
                    .subscribe(responseBody -> response[0] = responseBody,
                            throwable -> view.handleException(throwable)));

            return response[0].string();
        } catch (IOException e) {
            Log.getStackTraceString(e);
        }
        return null;
    }

    @Override
    public void postOneTimeKeys(AccountModel accountModel) {
        subscriptions.add(postOneTimeKeysInteractor.execute(accountModel)
                .subscribe(responseBody -> view.handleSuccess(responseBody.string()),
                        throwable -> view.handleException(throwable)));
    }

    @Override
    public void postSignedKey(AccountModel accountModel) {
        subscriptions.add(postSignedKeysInteractor.execute(accountModel)
                .subscribe(responseBody -> view.handleSuccess(responseBody.string()),
                        throwable -> view.handleException(throwable)));
    }

    @Override
    public void chooseAccount(String name) {
        subscriptions.add(chooseAccountInteractor.execute(accountMapper.mapToModel(accountStore.findAccountByName(name)))
                .subscribe(accountModel -> {
                    accountStore.setAccountActive(accountMapper.mapToEntity(accountModel));
                    view.handleSuccess(accountModel.getName() + " account chosen.");
                }, throwable -> view.handleException(throwable)));
    }

    @Override
    public void deleteAccount(String name) {
        subscriptions.add(deleteAccountInteractor.execute(accountMapper.mapToModel(accountStore.findAccountByName(name)))
                .subscribe(aBoolean -> view.handleSuccess("Account " + name + " deleted: " + aBoolean),
                        throwable -> view.handleException(throwable)));
    }

}
