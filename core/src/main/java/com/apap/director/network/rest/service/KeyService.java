package com.apap.director.network.rest.service;


import com.apap.director.db.realm.model.OneTimeKey;
import com.apap.director.db.realm.model.SignedKey;
import com.apap.director.db.realm.to.OneTimeKeyTO;
import com.apap.director.db.realm.to.SignedKeyTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KeyService {

    @POST(value = "/key/one_time")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> postOneTimeKeys(@Body List<OneTimeKeyTO> keys);

    @POST(value = "/key/signed")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> postSignedKeys(@Body List<SignedKeyTO> keys);

    @GET(value = "/key/one_time/{ownerId}")
    Call<OneTimeKey> getOneTimeKey(@Path("ownerId") String ownerId);

    @GET(value = "/key/signed/{ownerId}")
    Call<SignedKey> getSignedKey(@Path("ownerId") String ownerId);

}
