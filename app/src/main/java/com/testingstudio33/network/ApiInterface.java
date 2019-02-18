package com.testingstudio33.network;

import com.testingstudio33.model.MLogin;
import com.testingstudio33.model.MOtp;
import com.testingstudio33.model.MRegister;
import com.testingstudio33.model.Mark;
import com.testingstudio33.model.Test;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("")
    Call<Test> getlog();

    @FormUrlEncoded
    @POST("api/login")
    Call<MLogin> getlogin(
            @Field("phone") String phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/register")
    Call<MRegister> putRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("c_password") String c_password
    );

    @FormUrlEncoded
    @POST("api/sendotp.php")
    Call<MOtp> sendotp(
            @Field("otp_length") int otp_length,
            @Field("authkey") String authkey,
            @Field("mobile") int mobile,
            @Field("message") String message,
            @Field("sender") String sender,
            @Field("otp") int otp,
            @Field("otp_expiry") int otp_expiry,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api/retryotp.php")
    Call<MOtp> resendotp(
            @Field("authkey") String authkey,
            @Field("mobile") int mobile
    );

    @FormUrlEncoded
    @POST("api/verifyRequestOTP.php")
    Call<MOtp> verifyotp(
            @Field("authkey") String authkey,
            @Field("otp") int otp,
            @Field("mobile") int mobile
    );

    @FormUrlEncoded
    @POST("api/attendlists")
    Call<Mark> mark_attend(
            @Field("user_id") int user_id,
            @Field("present_status") int present_status,
            @Field("present_at") String present_at
    );


}