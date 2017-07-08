package com.apap.director.client.domain.interactor.register;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.exception.DuplicateException;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.OneTimeKeyModel;
import com.apap.director.client.domain.model.SignedKeyModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.util.Base64Util;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * Created by Alicja Michniewicz
 */

public class CreateAccountInteractor extends BaseInteractor<AccountModel, String> {

    private final String NAME_COLUMN = "name";

    private AccountRepository accountRepository;
    private GenerateSignedKeyInteractor generateSignedKeyInteractor;
    private GenerateOneTimeKeysInteractor generateOneTimeKeysInteractor;

    @Inject
    public CreateAccountInteractor(GenerateSignedKeyInteractor generateSignedKeyInteractor, GenerateOneTimeKeysInteractor generateOneTimeKeysInteractor) {
        this.generateSignedKeyInteractor = generateSignedKeyInteractor;
        this.generateOneTimeKeysInteractor = generateOneTimeKeysInteractor;
    }

    @Override
    protected Observable<AccountModel> buildObservable(String name) {
        if(isDuplicate(name)) {
            return createError(name);
        }
        else {
            return createAccount(name);
        }
    }

    private Observable<AccountModel> createError(String name) {
        return Observable.error(new DuplicateException(AccountEntity.class.getSimpleName(), NAME_COLUMN, name));
    }

    private Observable<AccountModel> createAccount(String name) {
        final AccountModel account = preconfigureAccount(name);

        return Observable.zip(generateSignedKeyInteractor.execute(account),
                generateOneTimeKeysInteractor.execute(account),
                new BiFunction<SignedKeyModel, List<OneTimeKeyModel>, AccountModel>() {
                    @Override
                    public AccountModel apply(@NonNull SignedKeyModel signedKeyModel, @NonNull List<OneTimeKeyModel> oneTimeKeyModels) throws Exception {
                        account.setSignedKey(signedKeyModel);
                        account.setOneTimeKeys(oneTimeKeyModels);

                        return account;
                    }
                });
    }

    private AccountModel preconfigureAccount(String name) {
        AccountModel accountModel = new AccountModel();
        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();

        accountModel.setKeyPair(identityKeyPair.serialize());
        accountModel.setKeyBase64(Base64Util.convertToBase64(identityKeyPair.getPublicKey()));
        accountModel.setRegistrationId(KeyHelper.generateRegistrationId(false));
        accountModel.setCookie(null);
        accountModel.setName(name);

        return accountModel;
    }

    private boolean isDuplicate(String name) {
        return accountRepository.getAccount(name) != null;
    }
}
