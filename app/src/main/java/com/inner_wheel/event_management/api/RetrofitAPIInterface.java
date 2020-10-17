package com.inner_wheel.event_management.api;

import com.inner_wheel.event_management.api.models.GetCompetitions;
import com.inner_wheel.event_management.api.models.ParentLogin;
import com.inner_wheel.event_management.api.models.ParticipantsResponse;
import com.inner_wheel.event_management.api.models.RegistrationResponse;
import com.inner_wheel.event_management.api.models.SelectCompetition;
import com.inner_wheel.event_management.api.models.SignUpResponse;
import com.inner_wheel.event_management.api.models.TransactionInitiate;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("competition/all")
    Call<GetCompetitions> getCompetitionList(@Header("token") String token);

    @GET("competition/{id}")
    Call<SelectCompetition> getSelectedCompetition(@Header("token") String token, @Path("id") String id);

    @FormUrlEncoded
    @POST("participant/register")
    Call<Object> addParticipant(@Header("token") String token,
                          @Field("name") String childName,
                          @Field("age") String age,
                          @Field("school") String school);

    @FormUrlEncoded
    @POST("participant/competition-registration")
    Call<RegistrationResponse> registerParticipant(@Header("token") String token,
                                                   @Field("age_group_id") String ageGroupID,
                                                   @Field("participant_id") String participantID,
                                                   @Field("transaction_id") String transactionID);

    @GET("participant/fetch")
    Call<ParticipantsResponse> getParticipants(@Header("token") String token);

    @FormUrlEncoded
    @POST
    Call<TransactionInitiate> startTransaction(@Header("token") String token,
                                               @Field("competition_id") String competitionID,
                                               @Field("participant_id") String participantID,
                                               @Field("amount") String amount);

    @FormUrlEncoded
    @PUT
    Call<TransactionInitiate> updateTransaction(@Header("token") String token,
                                    @Field("id") String id,
                                    @Field("transaction_id") String transactionID,
                                    @Field("status") String status);
}
