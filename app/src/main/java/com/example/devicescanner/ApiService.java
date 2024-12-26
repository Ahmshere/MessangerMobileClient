package com.example.devicescanner;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/users/register")
    Call<Void> registerUser(@Body User user);
}
