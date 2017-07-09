package com.apap.director.client.data.net.mapper;

import android.util.Log;

import com.apap.director.client.data.net.mapper.base.BaseTOMapper;
import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.domain.model.OneTimeKeyModel;

import java.io.UnsupportedEncodingException;

/**
 * Created by Adam Poterałowicz
 */

public class OneTimeKeyTOMapper extends BaseTOMapper<OneTimeKeyModel, OneTimeKeyTO> {

    @Override
    public OneTimeKeyTO mapToTO(OneTimeKeyModel model) {
        OneTimeKeyTO oneTimeKeyTO = new OneTimeKeyTO();

        try {
            if (model == null)
                return null;

            oneTimeKeyTO.setOneTimeKeyId(model.getOneTimeKeyId());
            oneTimeKeyTO.setKeyId(model.getId());
            oneTimeKeyTO.setKeyBase64(new String(model.getSerializedKey(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.getStackTraceString(e);
        }

        return oneTimeKeyTO;
    }

    @Override
    public OneTimeKeyModel mapToModel(OneTimeKeyTO to) {
        OneTimeKeyModel oneTimeKey = new OneTimeKeyModel();

        try {
            if (to == null)
                return null;

            oneTimeKey.setOneTimeKeyId(to.getOneTimeKeyId());
            oneTimeKey.setId(to.getKeyId());
            oneTimeKey.setSerializedKey(to.getKeyBase64().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.getStackTraceString(e);
        }

        return oneTimeKey;
    }
}
