package com.apap.director.client.domain.repository;

import com.apap.director.client.data.net.rest.service.LoginDetails;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public interface LoginRepository {

    Observable<String> getCode(String userId);

    Observable<ResponseBody> login(LoginDetails details);
}
