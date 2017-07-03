package com.apap.director.client.data.net.rest.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST(value = "/user/{userId}")
    Call<ResponseBody> signUp(@Path("userId") String userId);

    @GET(value = "/user/code/{userId}")
    Call<String> requestCode(@Path("userId") String userId);

    @POST(value = "/login")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> login(@Body LoginDetails details);

}
