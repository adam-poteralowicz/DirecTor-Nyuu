package com.apap.director.client.data.net.rest.service;


import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KeyService {

    @POST(value = "/key/one_time")
    @Headers("Content-Type: application/json")
    Observable<ResponseBody> postOneTimeKeys(@Body List<OneTimeKeyTO> keys, @Header("Cookie") String cookiez);

    @POST(value = "/key/signed")
    @Headers("Content-Type: application/json")
    Observable<ResponseBody> postSignedKeys(@Body SignedKeyTO keyTO, @Header("Cookie") String cookiez);

    @GET(value = "/key/one_time/{ownerId}")
    Observable<OneTimeKeyTO> getOneTimeKey(@Path("ownerId") String ownerId);

    @GET(value = "/key/signed/{ownerId}")
    Observable<SignedKeyTO> getSignedKey(@Path("ownerId") String ownerId);

}
