package com.apap.director.client.domain.interactor.register;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.mapper.OneTimeKeyMapper;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.OneTimeKeyModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.OneTimeKeyRepository;

import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * Created by Alicja Michniewicz
 */

public class GenerateOneTimeKeysInteractor extends BaseInteractor<List<OneTimeKeyModel>, AccountModel> {

    private final int MAX_KEYS = 100;

    private AccountRepository accountRepository;
    private OneTimeKeyRepository oneTimeKeyRepository;

    @Inject
    public GenerateOneTimeKeysInteractor(AccountRepository accountRepository, OneTimeKeyRepository oneTimeKeyRepository) {
        this.accountRepository = accountRepository;
        this.oneTimeKeyRepository = oneTimeKeyRepository;
    }

    @Override
    public Observable<List<OneTimeKeyModel>> buildObservable(AccountModel account) {
        return Observable.zip(oneTimeKeyRepository.findNextId(),
                accountRepository.findLastOneTimeKeyId(account),
                new BiFunction<Long, Integer, List<OneTimeKeyModel>>() {
                    @Override
                    public List<OneTimeKeyModel> apply(@NonNull Long dbId, @NonNull Integer last) throws Exception {
                        OneTimeKeyMapper mapper = new OneTimeKeyMapper();

                        List<PreKeyRecord> oneTimeKeyRecords = KeyHelper.generatePreKeys(last, MAX_KEYS - last);
                        return mapper.mapRecordListToModelList(oneTimeKeyRecords, dbId);
                    }
                });
    }
}
