package com.dms.beinone.application;

import com.dms.beinone.application.models.Account;
import com.dms.beinone.application.models.ApplyStatus;
import com.dms.beinone.application.models.Class;
import com.dms.beinone.application.models.Meal;
import com.dms.beinone.application.models.Token;
import com.dms.beinone.application.models.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by BeINone on 2017-07-10.
 */

public interface DMSService {

    int HTTP_OK = 200;
    int HTTP_CREATED = 201;
    int HTTP_NO_CONTENT = 204;
    int HTTP_BAD_REQUEST = 400;
    int HTTP_CONFLICT = 409;

    int HTTP_INTERNAL_SERVER_ERROR = 500;

    String SERVER_STUDENT_URL = "http://dsm2015.cafe24.com:3000";


    //Student
    @POST("signup")
    Call<Void> signup(@Field("uuid") String uid,@Field("id") String id,@Field("pw") String pw);

    @FormUrlEncoded
    @POST("/auth/student")
    Call<Token> login(@Field("id") String id , @Field("pw") String pw);

    @GET("/faq")
    Call<JsonArray> loadFag();

    @GET("/faq/{id}")
    Call<JsonObject> loadFag_detail(@Path("id") String id);

    @GET("/notice")
    Call<JsonArray> loadNotice();

    @GET("/notice/{id}")
    Call<JsonObject> loadNotice_detail(@Path("id") String id);

    @GET("/rule")
    Call<JsonArray> loadRule();

    @GET("/meal/{date}")
    Call<JsonObject> loadMeal(@Path("date") String date);

    @GET("/rule/{id}")
    Call<JsonObject> loadRule_detail(@Path("id") String id);

    @GET("/extension/map/11")
    Call<JsonArray> loadExtensionClass_11(@Query("class") int clazz);

    @GET("/extension/map/12")
    Call<JsonArray> loadExtensionClass_12(@Query("class") int clazz);

    //Header 필요한 요청

    @FormUrlEncoded
    @POST("/extension/11")
    Call<Void> applyExtension_11(@Header("Authorization") String authorization, @Field("class") int clazz, @Field("seat") int seat);

    @GET("/extension/11")
    Call<JsonObject> applyExtensionStatus_11(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/extension/12")
    Call<Void> applyExtension_12(@Header("Authorization") String authorization, @Field("class") int clazz, @Field("seat") int seat);

    @GET("/extension/12")
    Call<ApplyStatus> applyExtensionStatus_12(@Header("Authorization") String authorization);

    @GET("/mypage")
    Call<Account> loadMyPage(@Header("Authorization") String authorization);

    @GET("/mypage")
    Call<JsonObject> loadMyPage_json(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/account/register/student")
    Call<Void> register(@Field("uid") String uid, @Field("id") String id, @Field("password") String password);

    @FormUrlEncoded
    @PATCH("/change/pw")
    Call<Void> change(@Field("exist") String exist, @Field("change") String change);

    @FormUrlEncoded
    @POST("/goingout")
    Call<Void> applyGoingout(@Header("Authorization") String authorization,@Field("sat") boolean sat, @Field("sun") boolean sun);

    @GET("/goingout")
    Call<JsonArray> applyGoingoutStatus(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/stay")
    Call<Void> applyStay(@Header("Authorization") String authorization,@Field("value") int value);

    @GET("/stay")
    Call<JsonObject> applyStayStatus(@Header("Authorization") String authorization);

    @GET("/apply/all")
    Call<ApplyStatus> loadApplyStatus();

    @POST("/survey/")
    Call<Void> uploadSurvey(@Field("id") int id);

    @GET("/survey")
    Call<JsonObject> loadSurveyList();

    @GET("/survey/")
    Call<JsonObject> loadSurvey(@Query("id")int id);

}
