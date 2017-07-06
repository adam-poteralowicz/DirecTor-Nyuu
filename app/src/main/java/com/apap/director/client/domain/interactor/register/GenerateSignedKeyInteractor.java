package com.apap.director.client.domain.interactor.register;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.SignedKeyModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.SignedKeyRepository;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * Created by Alicja Michniewicz
 */

public class GenerateSignedKeyInteractor extends BaseInteractor<SignedKeyModel,AccountModel> {

    private AccountRepository accountRepository;
    private SignedKeyRepository signedKeyRepository;


    @Override
    public Observable<SignedKeyModel> buildObservable(final AccountModel accountModel) {
        return Observable.zip(signedKeyRepository.findNextId(),
                accountRepository.findLastSignedKeyId(accountModel),
                new BiFunction<Long, Integer, SignedKeyModel>() {
                    @Override
                    public SignedKeyModel apply(@NonNull Long dbId, @NonNull Integer keyId) throws Exception {
                        SignedKeyModel model = createEmptySignedKey(dbId, keyId);
                        SignedPreKeyRecord record = KeyHelper.generateSignedPreKey(new IdentityKeyPair(accountModel.getKeyPair()), keyId);
                        model.setSerializedKey(record.serialize());

                        return model;
                    }
                });
    }

    private SignedKeyModel createEmptySignedKey(long dbId, int keyId) {
        SignedKeyModel signedKeyModel = new SignedKeyModel();
        signedKeyModel.setId(dbId);
        signedKeyModel.setSignedKeyId(keyId);

        return signedKeyModel;
    }
}
