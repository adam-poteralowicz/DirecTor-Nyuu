package com.apap.director.client.domain.interactor.login;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.LoginRepository;
import com.apap.director.client.domain.util.EncryptionUtil;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class SignCodeInteractor extends BaseInteractor<String, String> {

    private AccountRepository accountRepository;
    private LoginRepository loginRepository;

    public SignCodeInteractor(AccountRepository accountRepository, LoginRepository loginRepository) {
        this.accountRepository = accountRepository;
        this.loginRepository = loginRepository;
    }

    @Inject
    public SignCodeInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    protected Observable<String> buildObservable(String code) {
        return accountRepository.getActiveAccount()
                .flatMap(account -> signKey(account, code));
    }

    private Observable<String> signKey(AccountModel account, String code) throws InvalidKeyException {
        return Observable.just(EncryptionUtil.signMessage(new IdentityKeyPair(account.getKeyPair()), code));
    }
}
