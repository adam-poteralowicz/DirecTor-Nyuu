package com.apap.director.client.data.repository;

import com.apap.director.client.data.net.rest.service.LoginDetails;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.domain.repository.LoginRepository;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Alicja Michniewicz
 */

public class LoginRepositoryImpl implements LoginRepository {

    private RestAccountService restAccountService;

    public LoginRepositoryImpl(RestAccountService restAccountService) {
        this.restAccountService = restAccountService;
    }

    @Override
    public Observable<String> getCode(String userId) {
        return restAccountService.requestCode(userId);
    }

    @Override
    public Observable<ResponseBody> login(LoginDetails details) {
        return null;
    }

}
