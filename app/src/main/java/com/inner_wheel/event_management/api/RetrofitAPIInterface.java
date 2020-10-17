package com.inner_wheel.event_management.api;

import com.inner_wheel.event_management.api.models.GetCompetitions;
import com.inner_wheel.event_management.api.models.ParentLogin;
import com.inner_wheel.event_management.api.models.ParticipantsResponse;
import com.inner_wheel.event_management.api.models.SelectCompetition;
import com.inner_wheel.event_management.api.models.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitAPIInterface {

    @FormUrlEncoded
    @POST("auth/login/user")
    Call<ParentLogin> getParentLoginToken(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/sign-up/user")
    Call<SignUpResponse> signUpParent(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("city") String city,
                                      @Field("state") String state,
                                      @Field("address") String address,
                                      @Field("phone_number") String phoneNumber,
                                      @Field("password") String password);

    @FormUrlEncoded
    @POST("participant/register")
    Call<Object> addParticipant(@Header("token") String token,
                          @Field("name") String childName,
                          @Field("age") String age,
                          @Field("school") String school);

    @GET("participant/fetch")
    Call<ParticipantsResponse> getParticipants(@Header("token") String token);

    @GET("competition/all")
    Call<GetCompetitions> getCompetitionList(@Header("token") String token);

    @GET("competition/{id}")
    Call<SelectCompetition> getSelectedCompetition(@Header("token") String token, @Path("id") String id);
}
