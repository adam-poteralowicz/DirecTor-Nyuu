package com.apap.director.client.data.db.mapper;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.domain.model.AccountModel;

import javax.inject.Inject;

/**
 * Created by Alicja Michniewicz
 */

public class AccountMapper extends BaseMapper<AccountModel, AccountEntity> {

    private OneTimeKeyMapper oneTimeKeyMapper;
    private SignedKeyMapper signedKeyMapper;

    @Inject
    public AccountMapper(OneTimeKeyMapper oneTimeKeyMapper, SignedKeyMapper signedKeyMapper) {
        this.oneTimeKeyMapper = oneTimeKeyMapper;
        this.signedKeyMapper = signedKeyMapper;
    }

    @Override
    public AccountEntity mapToEntity(AccountModel model) {
        if(model == null)
            return null;

        AccountEntity entity = new AccountEntity();

        entity.setName(model.getName());
        entity.setKeyPair(model.getKeyPair());
        entity.setRegistrationId(model.getRegistrationId());
        entity.setMasterPassword(model.getMasterPassword());
        entity.setKeyBase64(model.getKeyBase64());
        entity.setActive(model.isActive());
        entity.setCookie(model.getCookie());
        entity.setRegistered(model.isRegistered());
        entity.setOneTimeKeys(mapToRealmList(oneTimeKeyMapper, model.getOneTimeKeys()));
        entity.setSignedKey(signedKeyMapper.mapToEntity(model.getSignedKey()));

        return entity;
    }

    @Override
    public AccountModel mapToModel(AccountEntity entity) {
        if(entity == null)
            return null;

        AccountModel model = new AccountModel();

        model.setName(entity.getName());
        model.setKeyPair(entity.getKeyPair());
        model.setRegistrationId(entity.getRegistrationId());
        model.setMasterPassword(entity.getMasterPassword());
        model.setKeyBase64(entity.getKeyBase64());
        model.setActive(entity.isActive());
        model.setCookie(entity.getCookie());
        model.setRegistered(entity.isRegistered());
        model.setOneTimeKeys(mapToList(oneTimeKeyMapper, entity.getOneTimeKeys()));
        model.setSignedKey(signedKeyMapper.mapToModel(entity.getSignedKey()));

        return model;
    }
}
