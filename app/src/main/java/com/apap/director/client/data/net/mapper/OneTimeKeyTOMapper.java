package com.apap.director.client.data.net.mapper;

import com.apap.director.client.data.net.mapper.base.BaseTOMapper;
import com.apap.director.client.data.net.model.OneTimeKey;
import com.apap.director.client.data.net.to.OneTimeKeyTO;

/**
 * Created by Adam Poterałowicz
 */

public class OneTimeKeyTOMapper extends BaseTOMapper<OneTimeKey, OneTimeKeyTO> {

    @Override
    public OneTimeKeyTO mapToTO(OneTimeKey model) {
        if (model == null)
            return null;

        OneTimeKeyTO oneTimeKeyTO = new OneTimeKeyTO();
        oneTimeKeyTO.setOneTimeKeyId(model.getOneTimeKeyId());
        oneTimeKeyTO.setKeyId(model.getKeyId());
        oneTimeKeyTO.setKeyBase64(model.getKeyBase64());

        return oneTimeKeyTO;
    }

    @Override
    public OneTimeKey mapToModel(OneTimeKeyTO to) {
        if (to == null)
            return null;

        OneTimeKey oneTimeKey = new OneTimeKey();
        oneTimeKey.setOneTimeKeyId(to.getOneTimeKeyId());
        oneTimeKey.setKeyId(to.getKeyId());
        oneTimeKey.setKeyBase64(to.getKeyBase64());

        return oneTimeKey;
    }
}
