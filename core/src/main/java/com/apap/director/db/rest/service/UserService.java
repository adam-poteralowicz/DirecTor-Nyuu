package com.apap.director.db.rest.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST(value ="/{userId}")
    Call<ResponseBody> signUp(@Path("userId") String userId);

    @GET(value = "/code/{userId}")
    Call<String> requestCode(@Path("userId") String userId);

    @POST(value = "/login")
    Call<ResponseBody> login(@Body LoginDetails details);

}
