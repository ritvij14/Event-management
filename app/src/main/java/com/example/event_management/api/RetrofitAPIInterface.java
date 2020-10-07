package com.example.event_management.api;

import com.example.event_management.api.models.ParentLogin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitAPIInterface {

    @FormUrlEncoded
    @POST("auth/login/user")
    Call<ParentLogin> getParentLoginToken(@Field("email") String email, @Field("password") String password);
}
